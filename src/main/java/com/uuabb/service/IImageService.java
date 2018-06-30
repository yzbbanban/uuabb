package com.uuabb.service;

import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.vo.ImageVo;

import java.util.List;

/**
 * Created by brander on 2017/12/24
 */
public interface IImageService {
    ServerResponse<String> addImage(String path);

    ServerResponse<List<ImageVo>> getImages();

}
