package com.umc.lifesharing.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    public List<String> uploadReviewFiles(List<MultipartFile> multipartFiles){
        return uploadFiles(multipartFiles, reviewPath);
    }

    public List<String> uploadProductFiles(List<MultipartFile> multipartFiles){
        return uploadFiles(multipartFiles, productPath);
    }

    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String path){
        List<String> fileNameList = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String fileName = url + path + "/" + createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()){
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            fileNameList.add(fileName);
        });

        return fileNameList;
    }

    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기위해, "."의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
        }
    }


    public void deleteFile(String fileName){
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        System.out.println(bucket);
    }

    public void deleteProductImages(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            // 각 이미지 URL에서 파일명을 추출
            String fileName = extractFileNameFromUrl(imageUrl);

            // S3에서 해당 파일을 삭제
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        }
    }

    // 이미지 URL에서 파일명을 추출하는 메서드
    private String extractFileNameFromUrl(String imageUrl) {
        // URL에서 파일명을 추출하는 로직을 구현
        // 예시: https://lifesharing.s3.ap-northeast-2.amazonaws.com/product/12345.jpg -> 12345.jpg

        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    // 이미지 리스트를 받아서, 새로 추가된 이미지는 추가하고 삭제된 이미지는 삭제함
    public List<String> autoImagesUploadAndDelete(List<String> beforeImages, List<MultipartFile> multipartFiles, String path) {
        // 이미지 파일 이름만 추출
        List<String> beforeFileNames = beforeImages.stream()
                .map(imageUrl -> extractFileNameFromUrl(imageUrl))
                .collect(Collectors.toList());

        // 새로 추가된 이미지 업로드
        List<String> newFileNames = uploadFiles(multipartFiles, path);

        // 삭제된 이미지 삭제
        List<String> deletedFileNames = new ArrayList<>(beforeFileNames);
        deletedFileNames.removeAll(newFileNames);
        deleteProductImages(deletedFileNames);

        // 새로운 이미지 URL 설정
        return newFileNames.stream()
                .map(fileName -> url + path + "/" + fileName)
                .collect(Collectors.toList());
    }
}
