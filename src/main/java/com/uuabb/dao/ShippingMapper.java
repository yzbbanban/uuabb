package com.uuabb.dao;

import com.uuabb.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByIdUserId(@Param("shippingId") Integer shippingId,@Param("userId")Integer userId);

    int updateByIdUserId(Shipping shipping);

    List<Shipping> selectByUserId(Integer userId);
}