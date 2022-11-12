package com.block.kakaoLogin.Login;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,Long> {

    @Query(value = "select * from kakaologin where kakaoid = :kakaoId", nativeQuery = true)
    public UserEntity kakao(@Param("kakaoId") String kakaoId);
    //mybatis를 사용해서 만들어보자~
    @Query(value = "select count(*) from kakaologin where userid = :userid And userpw = :userpw", nativeQuery = true)
    int loginCheck(@Param("userid")String userid, @Param("userpw")String userpw);
}
