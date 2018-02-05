package com.uuabb.controller.protal;

import com.uuabb.common.Const;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.User;
import com.uuabb.service.ICartService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by brander on 2018/1/23
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping(value = "add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.add(user.getId(), productId, count);
    }

    @RequestMapping(value = "update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping(value = "delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse add(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.list(user.getId());
    }


    //全选
    @RequestMapping(value = "select_all.do")
    @ResponseBody
    public ServerResponse selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.selectOrUnSelectAll(user.getId(), null, Const.Cart.CHECKED);
    }


    //全反选
    @RequestMapping(value = "un_select_all.do")
    @ResponseBody
    public ServerResponse unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.selectOrUnSelectAll(user.getId(), null, Const.Cart.UNCHECKED);
    }

    //单独选
    @RequestMapping(value = "select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.selectOrUnSelectAll(user.getId(), productId, Const.Cart.CHECKED);
    }

    //单独反选
    @RequestMapping(value = "un_select.do")
    @ResponseBody
    public ServerResponse unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.selectOrUnSelectAll(user.getId(), productId, Const.Cart.UNCHECKED);
    }
    //查询当前购物车里面的产品数量
    @RequestMapping(value = "get_cart_product_count.do")
    @ResponseBody
    public ServerResponse getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("请重新登录");
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
