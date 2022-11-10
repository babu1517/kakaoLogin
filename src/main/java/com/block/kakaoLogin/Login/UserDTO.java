package com.block.kakaoLogin.Login;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {
    long usercode;
    String userid;
    String userpw;
    String username;
    String useremail;
    String kakaoid;

    public UserEntity toEntity() {
        return new UserEntity(usercode, userid, userpw, username,useremail, kakaoid);
    }
}
