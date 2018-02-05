package com.uuabb.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.uuabb.common.Const;
import com.uuabb.common.ResponseCode;
import com.uuabb.common.ServerResponse;
import com.uuabb.dao.CartMapper;
import com.uuabb.dao.ProductMapper;
import com.uuabb.pojo.Cart;
import com.uuabb.pojo.Product;
import com.uuabb.pojo.vo.CartProductVo;
import com.uuabb.pojo.vo.CartVo;
import com.uuabb.service.ICartService;
import com.uuabb.util.BigDecimalUtil;
import com.uuabb.util.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by brander on 2018/1/23
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {
        //根绝 productI 和 userId 获取购物车内容
        Cart cart = cartMapper.selectByUserIdProductId(userId, productId);
        if (cart == null) {//如果没有该人购物车相关产品，则创建一个
            cart = new Cart();
            cart.setChecked(Const.Cart.CHECKED);
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setUserId(userId);
            cartMapper.insert(cart);
        } else {//有则增加相关产品数据、数量
            cart.setChecked(Const.Cart.CHECKED);
            cart.setQuantity(count + cart.getQuantity());
            cartMapper.updateByPrimaryKey(cart);
        }
        return list(userId);

    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKey(cart);

        return list(userId);
    }

    @Override
    public ServerResponse deleteProduct(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId, productIdList);
        return list(userId);
    }

    @Override
    public ServerResponse list(Integer userId) {
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse selectOrUnSelectAll(Integer userId, Integer productId, Integer checked) {
        cartMapper.checkedOrUnCheckedProduct(userId, productId, checked);
        return list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }


    /**
     * 获取所有的购物车内容
     *
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        //获取购物车列表
        List<Cart> cartList = cartMapper.selectCartProductByUserId(userId);

        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cart : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cart.getProductId());
                //产品的库存数量
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());

                int buyLimitCount = 0;
                cartProductVo.setProductMainImage(product.getMainImage());
                cartProductVo.setProductName(product.getName());
                cartProductVo.setProductSubtitle(product.getSubtitle());
                cartProductVo.setProductStock(product.getStock());
                cartProductVo.setProductStatus(product.getStatus());
                cartProductVo.setProductPrice(product.getPrice());
                if (product.getStock() >= cart.getQuantity()) {//库存的数量大于购物车数量正常返回
                    buyLimitCount = cart.getQuantity();
                    cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                } else {
                    //购物车超出产品数量
                    buyLimitCount = product.getStock();
                    cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                    //更新购物车数量
                    Cart cartForQuantity = new Cart();
                    cartForQuantity.setId(cart.getId());
                    cartForQuantity.setQuantity(buyLimitCount);
                    cartMapper.updateByPrimaryKey(cartForQuantity);
                }

                cartProductVo.setQuantity(buyLimitCount);
                //计算总价
                cartProductVo.setProductTotalPrice(
                        BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));

                if (cart.getChecked() == Const.Cart.CHECKED) {
                    //如果已勾选，增加到整个购物车的总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);

            }

        }

        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVo.setAllChecked(getAllCheckedStatus(userId));
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);

        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckStatusByUserId(userId) == 0;

    }


}
