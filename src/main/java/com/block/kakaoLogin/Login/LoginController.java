package com.block.kakaoLogin.Login;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping(value = "/")
    public String main() {

        return "main";
    }

    @GetMapping(value = "/userInput")
    public String userInput() {
        return "userInput";
    }

    //유저 회원가입
    @PostMapping(value = "/inputSave")
    public String inputSave(UserDTO dto) {
        UserEntity entity = dto.toEntity();
        loginService.inputSave(entity);
        return "main";
    }

    @PostMapping(value = "/loginCheck")
    public String loginCheck(UserDTO dto, Model model) {
        String userid = dto.getUserid();
        String userpw = dto.getUserpw();
        log.info("usercode: " + dto.getUsercode());
        int result = loginService.loginCheck(userid, userpw);
        if (result == 1) {
            long usercode = dto.getUsercode();
            model.addAttribute("usercode", usercode);
            return "home";
        } else {
            return "main";
        }
    }

    //카카오 Rest API 키 접속
    @RequestMapping(value = "/getKakaoAuthUrl")
    public @ResponseBody String getKakaoAuthUrl(HttpServletRequest request) {
        String reqUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=fabbcd95969aa1ff7041fb0cdd0a096e"
                + "&redirect_uri=http://localhost:8686/oauth_kakao"
                + "&response_type=code";
        return reqUrl;
    }

    //카카오 연동정보 조회
    @RequestMapping(value = "/oauth_kakao", produces = "application/text; charset=UTF-8")
    public String oauthKakao(@RequestParam(value = "code", required = false) String code, Model model) {
        log.info("카카오 연동정보 조회 코드: " + code);
        String access_Token = loginService.getAccessToken(code);
        log.info("카카오 연동정보 토큰: " + access_Token);

        HashMap<String, Object> userInfo = loginService.getUserInfo(access_Token);
        log.info("카카오 유저정보 이메일: " + userInfo.get("email"));
        log.info("카카오 유저정보 닉네임: " + userInfo.get("nickname"));
        log.info("카카오 아이디: " + userInfo.get("kakaoId"));

        if (userInfo.get("kakaoId") != null) {
            String kakaoId = userInfo.get("kakaoId").toString();
            UserDTO dto = loginService.findById(kakaoId);
            log.info("dto 카톡아이디: "+dto.getKakaoid());
            if (dto.getKakaoid() != null) {
                model.addAttribute("entity", dto);
                log.info("userid: " + dto.getUserid());
                String userid = dto.getUserid();
                String userpw = dto.getUserpw();
                int result = loginService.loginCheck(userid, userpw);

                if (result == 1) {
                    long usercode = dto.getUsercode();
                    model.addAttribute("usercode", usercode);
                    return "home";
                }
                else {
                    model.addAttribute("userInfo", userInfo);
                    return "userInput";
                }

            }
            else if(dto.getKakaoid()==null) {
            String massage = "등록된 아이디가 없습니다. 회원가입 후 이용해주세요.";
            model.addAttribute("massage", massage);
                return "userInput";
            }

        }
        return "/";

    }
}

