package com.smhrd.bookor.member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private String userId;

    @Column(name="user_pw")
    private String userPw;

    @Column(name="user_nick")
    private String userNick;



}
