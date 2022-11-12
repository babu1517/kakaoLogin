package com.block.kakaoLogin.Login;

import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;


public interface LoginService {

    public String getAccessToken(String authorize_code);
    public HashMap<String, Object> getUserInfo(String access_Token);

    void inputSave(UserEntity entity);


    UserDTO findById(String kakaoId);

    int loginCheck(String userid, String userpw);
}
