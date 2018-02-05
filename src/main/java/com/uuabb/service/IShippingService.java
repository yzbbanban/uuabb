package com.uuabb.service;

import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.Shipping;

/**
 * Created by brander on 2018/2/3
 */
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse del(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse list(Integer userId, int pageNum, int pageSize);
}
