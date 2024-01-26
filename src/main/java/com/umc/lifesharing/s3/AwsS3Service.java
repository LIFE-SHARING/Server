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
        });
        return fileNameList;
    }

    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public String createFileName(String fileName){
        String uniqueId = UUID.randomUUID().toString();
 //       String fileExtension = getFileExtension(fileName);

        return uniqueId + "_" + fileName;
    }

    public void deleteProductFile(String imageUrl) {
        String fileName = getFileNameFromUrl(imageUrl);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "product/" + fileName));
    }

    public void deleteReviewFile(String imageUrl) {
        String fileName = getFileNameFromUrl(imageUrl);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "review/" + fileName));
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
