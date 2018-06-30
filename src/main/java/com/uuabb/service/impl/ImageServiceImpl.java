package com.uuabb.service.impl;

import com.google.common.collect.Lists;
import com.uuabb.common.ServerResponse;
import com.uuabb.dao.ImageMapper;
import com.uuabb.pojo.Image;
import com.uuabb.pojo.vo.ImageVo;
import com.uuabb.service.IFileService;
import com.uuabb.service.IImageService;
import com.uuabb.util.FTPUtil;
import com.uuabb.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by brander on 2017/12/24
 */
@Service("iImageService")
public class ImageServiceImpl implements IImageService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public ServerResponse<String> addImage(String path) {
        Image image = new Image();
        image.setImageUrl(path);
        int row = imageMapper.insert(image);
        if (row > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse<List<ImageVo>> getImages() {
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix");

        List<Image> imageList = imageMapper.selectImageList();
        if (CollectionUtils.isEmpty(imageList)) {
            return ServerResponse.createByError();
        }
        List<ImageVo> imageVoList = new ArrayList<>();
        for (Image i : imageList) {
            ImageVo imageVo = new ImageVo();
            imageVo.setId(i.getId());
            imageVo.setImageUrl(url+i.getImageUrl());
            imageVo.setIp(i.getIp());
            imageVo.setCreateTime(i.getCreateTime());
            imageVoList.add(imageVo);
        }
        return ServerResponse.createBySuccess(imageVoList);
    }
}
