package com.umc.lifesharing.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.apiPayload.exception.handler.ReviewHandler;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.product.repository.ProductImageRepository;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.entity.ReviewImage;
import com.umc.lifesharing.review.rerpository.ReviewImageRepository;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.path.review}")
    private String reviewPath;

    @Value("${cloud.aws.s3.path.product}")
    private String productPath;

    private final AmazonS3 amazonS3;

    @Value("${s3.url}")
    private String url;

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ReviewImageRepository reviewImageRepository;

    public List<String> uploadReviewFiles(List<MultipartFile> multipartFiles){
        return uploadFiles(multipartFiles, reviewPath);
    }

    public List<String> uploadProductFiles(List<MultipartFile> multipartFiles){
        return uploadFiles(multipartFiles, productPath);
    }

    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String path){
        List<String> fileNameList = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String fileName = path + "/" + createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()){
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            fileNameList.add(amazonS3.getUrl(bucket, fileName).toString().split("com/")[1]);
            //fileNameList.add(fileName);
        });

        return fileNameList;
    }

    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public String createFileName(String fileName){
        String uniqueId = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(fileName);

        return uniqueId + "_" + fileName;
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기위해, "."의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
        }
    }

    public void deleteProductFile(String imageUrl) {
        String fileName = getFileNameFromUrl(imageUrl);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "product/" + fileName));
    }

    public void deleteReviewFile(String imageUrl) {
        String fileName = getFileNameFromUrl(imageUrl);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "review/" + fileName));
    }


    // 이미지 URL에서 파일명을 추출하는 메서드
    private String extractFileNameFromUrl(String imageUrl) {
        // URL에서 파일명을 추출하는 로직을 구현
        // 예시: https://lifesharing.s3.ap-northeast-2.amazonaws.com/product/12345.jpg -> 12345.jpg

        return imageUrl.substring(imageUrl.indexOf("_") + 1);
    }

    public List<String> updateReviewFiles(Long reviewId, List<MultipartFile> newFiles) {
        // 기존 리뷰의 이미지 URL 가져오기
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));

        List<String> existingFileNames = existingReview.getImages().stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());

        // 새로운 파일 업로드
        List<String> newFileNames = uploadFiles(newFiles, reviewPath);

        // 새로운 이미지 리스트가 비어있으면 기존 이미지 리스트 그대로 반환
        if (newFileNames.isEmpty()) {
            return existingFileNames;
        }

        // 기존 파일 중에서 새로운 파일에 없는 것은 삭제
        List<String> deletedFileNames = existingFileNames.stream()
                .filter(fileName -> !newFileNames.contains(fileName))
                .collect(Collectors.toList());

        // 삭제된 파일 삭제
        deleteReviewImages(deletedFileNames);

        // 새로운 파일과 기존 파일 합치기
        List<String> updatedFileNames = new ArrayList<>(existingFileNames);
        updatedFileNames.addAll(newFileNames);

        return updatedFileNames;
    }

    private void deleteReviewImages(List<String> fileNames) {
        for (String fileName : fileNames) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));

            // DB에서 해당 이미지 엔티티 찾기
            ReviewImage reviewImage = reviewImageRepository.findByImageUrl(fileName)
                    .orElseThrow(() -> new ProductHandler(ErrorStatus.NO_PRODUCT_IMAGE));

            // 이미지 엔티티 삭제
            reviewImageRepository.delete(reviewImage);
        }
    }

    private void deleteProductImages(List<String> fileNames) {
        for (String fileName : fileNames) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));

            // DB에서 해당 이미지 엔티티 찾기
            ProductImage productImage = productImageRepository.findByImageUrl(fileName)
                    .orElseThrow(() -> new ProductHandler(ErrorStatus.NO_PRODUCT_IMAGE));

            // 이미지 엔티티 삭제
            productImageRepository.delete(productImage);
        }
    }

    

    public List<String> updateProductFiles(Long productId, List<MultipartFile> newFiles) {
        // 기존 제품 조회
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        // 기존 이미지 삭제
//        deleteProductImages(existingProduct.getImages().stream()
//                .map(ProductImage::getImageUrl)
//                .collect(Collectors.toList()));

        // 기존 이미지 삭제
        List<ProductImage> existingImages = productImageRepository.findByProductId(existingProduct.getId());
        productImageRepository.deleteAll(existingImages);

        // 새로운 파일 업로드
        List<String> newFileNames = uploadFiles(newFiles, productPath);

        // 새로운 이미지 리스트가 없으면 기존 이미지 리스트를 유지하도록
        if (newFileNames.isEmpty()) {
            return existingProduct.getImages().stream()
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toList());
        }

//        // 새로운 이미지 추가
//        List<ProductImage> updatedImages = newFileNames.stream()
// //               .map(ProductImage::create)
//                .map(imageUrl -> ProductImage.create(existingProduct, imageUrl))
//                .collect(Collectors.toList());
//        existingProduct.setImages(updatedImages);
//
//        // 업데이트된 이미지로 기존 제품의 이미지 업데이트
////        productRepository.save(existingProduct);
//        productImageRepository.saveAll(updatedImages);

        return newFileNames;
    }

    private String getFileNameFromUrl(String imageUrl) {
        // URL에서 마지막 슬래시('/') 뒤의 문자열을 추출하여 파일 이름으로 사용
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        if (lastSlashIndex != -1 && lastSlashIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastSlashIndex + 1);
        } else {
            // 슬래시가 없거나 마지막에 위치한 경우 URL 전체를 반환
            return imageUrl;
        }
    }
}
