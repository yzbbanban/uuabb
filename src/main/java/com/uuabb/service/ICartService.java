package com.uuabb.service;

import com.uuabb.common.ServerResponse;

/**
 * Created by brander on 2018/1/23
 */
public interface ICartService {
    ServerResponse add(Integer userId,Integer productId, Integer count);

    ServerResponse update(Integer userId, Integer productId, Integer count);

    ServerResponse deleteProduct(Integer userId, String productIds);

    ServerResponse list(Integer userId);

    ServerResponse selectOrUnSelectAll(Integer userId,Integer productId,Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
