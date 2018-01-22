package com.uuabb.service;

import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.Category;

import java.util.List;

/**
 * Created by brander on 2017/12/18
 */
public interface ICategoryService {
    ServerResponse<String> addCategory(String categoryName, Integer parentId);
    ServerResponse<String> updateCategory(String categoryName, Integer parentId);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);
    ServerResponse<List<Integer>> selectChildrenParallelCategory(Integer categoryId);
}
