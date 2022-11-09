package com.block.kakaoLogin.Login;

import java.io.IOException;
import java.util.HashMap;


public interface LoginService {

    public String getAccessToken(String authorize_code);
    public HashMap<String, Object> getUserInfo(String access_Token) throws IOException;
}
