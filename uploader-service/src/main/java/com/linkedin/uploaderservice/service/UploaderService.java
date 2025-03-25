package com.linkedin.uploaderservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderService {

    // MultipartFile is the file object that we receive
    String upload(MultipartFile file);

}
