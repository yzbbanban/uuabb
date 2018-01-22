package com.uuabb.service;

import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String pasword);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str, String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username, String question, String answer);
    ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token);
    ServerResponse<String> restPassword(User user,String passwordOld,String passwordNew);
    ServerResponse<User> updateInformation(User user);
    ServerResponse<User> getInformation(User user);
    ServerResponse<String> checkAdminRole(User user);
}
