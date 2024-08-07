package com.smhrd.bookor.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {

     @Autowired
     private MemberService service;

    // JSON으로 넘어온 데이터를 자바 객체로 자동으로 형변환해줌.
    // 원리는? ..
    @PostMapping("/join")
    public void join(@RequestBody MemberDTO memberDto) {

        Member member = Member.builder()
                .userId(memberDto.getUserId())
                .userPw(memberDto.getUserPw())
                .userNick(memberDto.getUserNick())
                .build();


        System.out.println("넘겨받은 데이터 : " + memberDto);

        service.join(member);

    }

    @PostMapping("/login")
    public Member login(@RequestBody MemberDTO memberDto) {

        Member member = Member.builder()
                .userId(memberDto.getUserId())
                .userPw(memberDto.getUserPw())
                .build();



        System.out.println("Login 시도 시 넘겨받은 데이터 : " + memberDto);

        Member getMember = service.login(member);

        System.out.println("받은 값 : " + getMember);

        return getMember;
    }
}
