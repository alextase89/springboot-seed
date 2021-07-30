package com.cubans.awss3.controller;

import com.cubans.awss3.api.StorageS3ResponseDTO;
import com.cubans.awss3.configuration.Constant;
import com.cubans.awss3.service.StorageS3Service;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(Constant.APP_PATH)
public class StorageS3Controller {

    private final StorageS3Service storageS3Service;

    @Autowired
    public StorageS3Controller(StorageS3Service storageS3Service) {
        this.storageS3Service = storageS3Service;
    }

    @PostMapping("/storage")
    public StorageS3ResponseDTO storageFile(
        @ApiParam(name = "File to be storage on aws s3") @RequestParam("multipartFile") MultipartFile multipartFile) {

        return this.storageS3Service.storageFile(multipartFile);
    }
}
