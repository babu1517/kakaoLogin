package com.block.kakaoLogin.Login;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="kakaologin")
@SequenceGenerator(
        name="kakaologin_seq_Generator",
        sequenceName="kakaologin_seq",
        initialValue = 1,allocationSize = 1)
public class UserEntity {
    @Id
    @Column
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kakaologin_seq_Generator")
    long usercode;
    @Column
    String userid;
    @Column
    String userpw;
    @Column
    String username;
    @Column
    String useremail;
    @Column
    String kakaoid;
}
