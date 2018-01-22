package com.uuabb.service;

import com.github.pagehelper.PageInfo;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.Product;
import com.uuabb.pojo.vo.ProductDetailVo;

/**
 * Created by brander on 2017/12/19
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse getProductList(Integer pageNum, Integer pageSize);

    ServerResponse searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
