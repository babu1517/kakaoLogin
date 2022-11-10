package com.block.kakaoLogin.Login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserRepository userRepository;

    @Override
    public String getAccessToken(String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            // URL 연결은 입출력에 사용 될 수 있고, POST 혹은 PUT 요청을 하려면 setDoOutput을 true로 설정해야함.
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
//			POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=fabbcd95969aa1ff7041fb0cdd0a096e"); // 본인이 발급받은 key
            sb.append("&redirect_uri=http://localhost:8686/oauth_kakao"); // 본인이 설정해 놓은 경로
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("getAccess Response code: " + responseCode);

            // 요청을 통해 얻은 Json타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null){
                result += line;
            }
            log.info("getAccess Response Body : "+result);

            //Gson 라이브러리에 포함된 클래스로 Json 파싱 객체 생성
            JsonElement element = JsonParser.parseString(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            log.info("Access Token : "+access_Token);
            log.info("Refresh Token : "+refresh_Token);

            br.close();
            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return access_Token;
    }

    @Override
    public HashMap<String, Object> getUserInfo(String access_Token) {
        //요청하는 클라이언트 마다 가진 정보가 다를 수 있기에 HashMap 타입으로 선언.
        HashMap<String,Object> userInfo = new HashMap<String,Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //요청에 필요한 Header에 포함 될 내용
            conn.setRequestProperty("Authorization","Bearer "+access_Token);

            int responseCode = conn.getResponseCode();
            log.info("Response code: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null){
                result += line;
            }
            log.info("ResponseBody: "+result);

            JsonElement element = JsonParser.parseString(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String kakaoId = element.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            userInfo.put("access_Token",access_Token);
            userInfo.put("nickname",nickname);
            userInfo.put("email",email);
            userInfo.put("kakaoId",kakaoId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userInfo;
    }

    @Override
    public void inputSave(UserEntity entity) {
        userRepository.save(entity);
    }

    @Override
    public UserEntity findById(String kakaoId) {
        return  userRepository.kakao(kakaoId);
    }

    @Override
    public int loginCheck(String userid, String userpw) {
        return userRepository.loginCheck(userid, userpw);
    }

}
