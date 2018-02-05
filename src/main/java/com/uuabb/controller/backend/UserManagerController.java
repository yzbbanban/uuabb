package com.uuabb.controller.backend;

import com.uuabb.common.Const;
import com.uuabb.common.ResponseCode;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.User;
import com.uuabb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员后台登录
     *
     * @param name
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String name, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(name, password);
        if (response.isSuccess()) {
            User user = response.getData();
            //判断是否管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //这是 session 记录用户信息
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return ServerResponse.createByErrorMsg("不是管理员，无法登陆");
            }
        }
        return response;
    }

}
