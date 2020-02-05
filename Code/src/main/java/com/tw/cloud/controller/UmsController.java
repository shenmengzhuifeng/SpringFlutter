package com.tw.cloud.controller;

import cn.hutool.json.JSONUtil;
import com.tw.cloud.bean.CommonResp;
import com.tw.cloud.service.UmsService;
import com.tw.cloud.service.impl.UmsServiceIml;
import com.tw.cloud.utils.UnifyApiUri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 用户信息操作相关
 *
 * @author
 * @create 2020-02-05 4:10 PM
 **/
@RestController
public class UmsController {

    @Autowired
    private UmsService umsService;


    @RequestMapping(value = UnifyApiUri.UserApi.API_UPLOAD_GALLERY,
            method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadGallery(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return JSONUtil.parse(CommonResp.failed("file can not null")).toStringPretty();
        }
        // Get the file and save it somewhere
        return umsService.uploadGallery(file);
    }
}
