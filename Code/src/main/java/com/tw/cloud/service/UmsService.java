package com.tw.cloud.service;

import com.tw.cloud.bean.CommonResp;
import org.springframework.web.multipart.MultipartFile;

public interface UmsService {

    String uploadGallery(MultipartFile multipartFile);
}
