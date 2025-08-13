package com.unithon.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.repository.AdvertisementRepository;
import com.unithon.global.error.code.status.ErrorStatus;
import com.unithon.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    private final AdvertisementRepository advertisementRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 이미지 업로드 하기
     */
    public String uploadImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);

        amazonS3.putObject(putObjectRequest);

        return getPublicUrl(fileName);
    }

    @Transactional
    public FileDTO.UploadResponse uploadAndSave(MultipartFile file) throws IOException {

        String imageUrl = uploadImage(file);

        return new FileDTO.UploadResponse(imageUrl, file.getOriginalFilename(), file.getSize());
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
    }
}
