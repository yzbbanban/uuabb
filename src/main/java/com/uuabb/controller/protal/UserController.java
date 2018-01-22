package com.uuabb.controller.protal;

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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 用户登出
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 验证用户名邮箱
     *
     * @param str 用户名或邮箱
     * @param type 用户名 username 、邮箱 email
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息");

    }



    /**
     * 查询出修改密码的问题
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {

        return iUserService.selectQuestion(username);
    }

    /**
     * 检查问题答案是否错误
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {

        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 使用问题重置密码
     *
     * @param username
     * @param passwordNew
     * @param token
     * @return
     */
    @RequestMapping(value = "forget_rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token) {
        return iUserService.forgetRestPassword(username, passwordNew, token);
    }

    /**
     * 使用旧密码更新密码
     *
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> restPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return iUserService.restPassword(user, passwordOld, passwordNew);
        } else {
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息");
        }

    }

    /**
     * 更新用户信息
     *
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        //获取 session 中的 user 信息，判断是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息");
        }
        //设置 id，username
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        //存入更新好的用户信息到 session 中
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        //获取 session 中的 user 信息，判断是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,需要强制登录 status=10");
        }
        return iUserService.getInformation(user);

    }
}