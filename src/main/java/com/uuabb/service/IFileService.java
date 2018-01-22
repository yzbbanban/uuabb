package com.uuabb.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by brander on 2017/12/24
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
