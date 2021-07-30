package com.cubans.awss3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cubans.awss3.api.StorageS3ResponseDTO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class StorageS3Service {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    @Autowired
    public StorageS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public StorageS3ResponseDTO storageFile(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            PutObjectRequest putRequest = new PutObjectRequest(this.bucketName, multipartFile.getOriginalFilename(),
                new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

            log.info("Storing file with name {} to amazon s3", multipartFile.getOriginalFilename());
            this.amazonS3.putObject(putRequest);

            FileUtils.forceDelete(file);

            StorageS3ResponseDTO storageS3Response = new StorageS3ResponseDTO();
            storageS3Response.setFileName(multipartFile.getOriginalFilename());
            storageS3Response.setLocationUrl(this.getResourceUrl(multipartFile.getOriginalFilename()));

            return storageS3Response;
        } catch (IOException ex) {
            log.error("Error storing file {} on aws s3 caused by {}", multipartFile.getOriginalFilename(),
                ex.getMessage(), ex);
        }
        return new StorageS3ResponseDTO();
    }

    public String getResourceUrl(String fileName) {
        return ((AmazonS3Client) this.amazonS3).getResourceUrl(this.bucketName, fileName);
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }
}
