package com.block.kakaoLogin.Login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;
    @GetMapping(value="/")
    public String main(){

        return "main";
    }
    //카카오 Rest API 키 접속
    @PostMapping(value="/getKakaoAuthUrl")
    public @ResponseBody String getKakaoAuthUrl(HttpRequest request){
        String reqUrl = "https://kauth.kakao.com/oauth/authorize" + "?client_id=21016d43242ea7b653bdff592c5086f9"
                + "&redirect_uri=http://localhost:8686/shop/user/oauth_kakao" + "&response_type=code";
        return reqUrl;
    }
    //카카오 연동정보 조회
    @RequestMapping(value = "/oauth_kakao", produces = "application/text; charset=UTF-8")
    public String oauthKakao(@RequestParam(value="code",required = false)String code,Model model){
        log.info("카카오 연동정보 조회 코드: "+code);
        String access_Token = LoginService.getAccessToken(code);
    }
}
