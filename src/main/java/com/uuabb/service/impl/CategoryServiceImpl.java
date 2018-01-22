package com.uuabb.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.uuabb.common.ServerResponse;
import com.uuabb.dao.CategoryMapper;
import com.uuabb.pojo.Category;
import com.uuabb.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * Created by brander on 2017/12/18
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultCount = categoryMapper.insert(category);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("添加品类失败");
        }
        return ServerResponse.createBySuccessMsg("添加品类成功");
    }

    @Override
    public ServerResponse<String> updateCategory(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("更新品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("更新品类失败");
        }
        return ServerResponse.createBySuccessMsg("更新品类成功");

    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        if (parentId == null) {
            logger.info("参数错误");
        }

        List<Category> categories = categoryMapper.selectChildrenByParentId(parentId);
        if (CollectionUtils.isEmpty(categories)) {
            logger.info("未找到子分类信息");
        }
        return ServerResponse.createBySuccess(categories);

    }

    /**
     * 递归查询本节点的 id 和孩子的 id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> selectChildrenParallelCategory(Integer categoryId) {
        if (categoryId == null) {
            logger.info("参数错误");
        }

        Set<Category> categorySet= Sets.newHashSet();
        findChildrenCategory(categorySet,categoryId);

        List<Integer> categoryIdList= Lists.newArrayList();

        for (Category c :categorySet) {
            categoryIdList.add(c.getId());
        }


        return ServerResponse.createBySuccess(categoryIdList);

    }

    private Set<Category> findChildrenCategory(Set<Category> categories,Integer categoryId) {
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categories.add(category);
        }
        List<Category> categoryList=categoryMapper.selectChildrenByParentId(categoryId);
        for (Category categoryItem:categoryList) {
            findChildrenCategory(categories,categoryItem.getId());
        }
        return categories;

    }


}
