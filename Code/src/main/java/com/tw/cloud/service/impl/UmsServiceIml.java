package com.tw.cloud.service.impl;

import cn.hutool.json.JSONUtil;
import com.tw.cloud.bean.CommonResp;
import com.tw.cloud.bean.UmsGallery;
import com.tw.cloud.bean.UmsGalleryExample;
import com.tw.cloud.mapper.UmsGalleryMapper;
import com.tw.cloud.service.UmsService;
import com.tw.cloud.utils.JwtTokenUtil;
import com.tw.common.utils.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * 用户信息相关service
 *
 * @author
 * @create 2020-02-05 4:13 PM
 **/
@Service
public class UmsServiceIml implements UmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UmsGalleryMapper umsGalleryMapper;

    @Value("${file.UPLOADED_FOLDER}")
    private String UPLOADED_FOLDER;
    @Value("${file.UPLOADED_PATH}")
    private String UPLOADED_PATH;

    @Value("${server.port}")
    private String port;

    @Override
    public String uploadGallery(MultipartFile file) {
        try {
            UserDetails userDetails = JwtTokenUtil.getUserDetails();
            byte[] bytes = file.getBytes();
            String tempUrl = UUID.randomUUID() + file.getOriginalFilename();
            String pathString = UPLOADED_FOLDER + tempUrl;
            Path path = Paths.get(pathString);
            Files.write(path, bytes);
            UmsGallery umsGallery = new UmsGallery();
            umsGallery.setCreateTime(new Date());
            umsGallery.setUserId(Integer.parseInt(userDetails.getUsername()));
            umsGallery.setUrl("http://" + NetworkUtils.getLocalHost() + ":" + port + UPLOADED_PATH + tempUrl);
            umsGalleryMapper.insert(umsGallery);
            return JSONUtil.parse(CommonResp.success(umsGallery.getUrl())).toStringPretty();
        } catch (IOException e) {
            e.printStackTrace();
            return JSONUtil.parse(CommonResp.failed(e.getMessage())).toStringPretty();
        }
    }
}
