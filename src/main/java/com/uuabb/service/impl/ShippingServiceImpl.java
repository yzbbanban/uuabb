package com.uuabb.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.uuabb.common.ServerResponse;
import com.uuabb.dao.ShippingMapper;
import com.uuabb.pojo.Shipping;
import com.uuabb.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by brander on 2018/2/3
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int row = shippingMapper.insert(shipping);

        if (row > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功", result);
        }
        return ServerResponse.createByErrorMsg("新建地址失败");
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        int row = shippingMapper.deleteByIdUserId(shippingId, userId);
        if (row > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMsg("删除地址失败");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int row = shippingMapper.updateByIdUserId(shipping);
        if (row > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("更新地址成功", result);
        }
        return ServerResponse.createByErrorMsg("更新地址失败");
    }

    @Override
    public ServerResponse list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
