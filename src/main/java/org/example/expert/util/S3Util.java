package org.example.expert.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.FileIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "S3Util")
@Component
@RequiredArgsConstructor
public class S3Util {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String fileMaxSize;

    // 파일 삭제
    public void deleteFile(String fileUrl) {
        String[] urlParts = fileUrl.split("/");
        String fileBucket = urlParts[2].split("\\.")[0];

        if (!fileBucket.equals(bucket))
            throw new FileIOException("이미지가 이미 존재합니다.");

        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));

        if (!amazonS3.doesObjectExist(bucket, objectKey))
            throw new FileIOException("삭제할 이미지가 존재하지 않습니다.");

        try {
            amazonS3.deleteObject(bucket, objectKey);
        } catch (AmazonS3Exception e) {
            throw new FileIOException("S3 파일 삭제 중 오류 발생");
        } catch (SdkClientException e) {
            throw new FileIOException("SDK 오류로 인한 파일 삭제 실패");
        } catch (Exception e) {
            throw new FileIOException("파일 삭제 중 알 수 없는 오류 발생");
        }
    }

    // 단일 파일 저장
    public String saveFile(String userId, MultipartFile file) {
        String randomFilename = createFileName(userId, file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            throw new FileIOException("S3 파일 업로드 중 오류 발생");
        } catch (SdkClientException e) {
            throw new FileIOException("SDK 오류로 인한 파일 업로드 실패");
        } catch (Exception e) {
            throw new FileIOException("파일 업로드 중 알 수 없는 오류 발생");
        }

        return amazonS3.getUrl(bucket, randomFilename).toString();
    }

    // 랜덤파일명 생성 (파일명 중복 방지)
    private String createFileName(String userId, MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String fileExt = validateFileExtension(fileName);
        return userId + "_" + fileName + "." + fileExt;
    }

    // 파일 확장자 체크
    private String validateFileExtension(String originalFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "jpeg");
        if (!allowedExtensions.contains(fileExtension))
            throw new FileIOException("지원하지 않는 확장자");
        return fileExtension;
    }

    // 요청에 중복되는 파일 여부 확인
//    private boolean isDuplicate(String path) {
//        String fileName = multipartFile.getOriginalFilename();
//        Long fileSize = multipartFile.getSize();
//
//        if (uploadedFileNames.contains(fileName) && uploadedFileSizes.contains(fileSize)) {
//            return true;
//        }
//
//        uploadedFileNames.add(fileName);
//        uploadedFileSizes.add(fileSize);
//
//        return false;
//    }
}
