package com.uuabb.dao;

import com.uuabb.pojo.Image;
import com.uuabb.pojo.Product;

import java.util.List;

/**
 * Created by brander on 2018/6/28
 */
public interface ImageMapper {
    int insert(Image image);

    List<Image> selectImageList();
}
