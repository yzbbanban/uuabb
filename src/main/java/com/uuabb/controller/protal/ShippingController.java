package com.uuabb.controller.protal;

import com.uuabb.common.Const;
import com.uuabb.common.ResponseCode;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.Shipping;
import com.uuabb.pojo.User;
import com.uuabb.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by brander on 2018/2/3
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping(value = "add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping(value = "update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        return iShippingService.update(user.getId(), shipping);
    }


    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
