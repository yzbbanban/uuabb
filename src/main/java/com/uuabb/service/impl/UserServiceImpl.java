package com.uuabb.service.impl;

import com.uuabb.common.Const;
import com.uuabb.common.ServerResponse;
import com.uuabb.common.TokenCache;
import com.uuabb.dao.UserMapper;
import com.uuabb.pojo.User;
import com.uuabb.service.IUserService;
import com.uuabb.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);

        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        // 密码登陆 MD5
        String pwd = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, pwd);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //设置权限
        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccessMsg("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            } else {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        return ServerResponse.createBySuccessMsg("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMsg("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        //System.out.printf("username: "+username+","+question+","+answer);
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //说明问题和答案是这个用户的并且正确
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("问题答案错误");

    }

    @Override
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token) {
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMsg("参数错误，请重新发送");
        }

        String forgetToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMsg("token已过期");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMsg("密码更新成功");
            }
        } else {
            return ServerResponse.createByErrorMsg("token 错误，请重新获取重置密码的 token");

        }

        return ServerResponse.createByErrorMsg("密码更新失败请重试");
    }

    @Override
    public ServerResponse<String> restPassword(User user, String passwordOld, String passwordNew) {
        String md5PasswordOld = MD5Util.MD5EncodeUtf8(passwordOld);
        int resultCount = userMapper.checkPassword(md5PasswordOld, user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int row = userMapper.updateByPrimaryKeySelective(user);
        if (row > 0) {
            return ServerResponse.createBySuccessMsg("密码更新成功");
        }

        return ServerResponse.createByErrorMsg("密码更新失败，请重试");

    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailById(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMsg("email已经存在，请更换 email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setQuestion(updateUser.getQuestion());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }

        return ServerResponse.createByErrorMsg("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(User user) {
        User currentUser = userMapper.selectByPrimaryKey(user.getId());
        if (currentUser == null) {
            return ServerResponse.createByErrorMsg("无此用户");
        }
        currentUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", currentUser);

    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMsg("该用户不是管理员");
        }
    }
}
