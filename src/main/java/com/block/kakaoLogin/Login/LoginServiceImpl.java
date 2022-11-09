package com.block.kakaoLogin.Login;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public String getAccessToken(String authorize_code) {
        String access_token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(conn.getOutputStream())));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=21016d43242ea7b653bdff592c5086f9");
            sb.append("&redirect_uri=http://localhost:8686/login");
            sb.append("&code="+authorize_code);
            bw.write(sb.toString());
            bw.flush();
            //결과 코드가 200이라면 성공

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
