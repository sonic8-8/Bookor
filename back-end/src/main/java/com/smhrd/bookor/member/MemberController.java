package com.smhrd.bookor.member;

import ch.qos.logback.core.net.SyslogOutputStream;
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

    // 회원가입
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

    // 로그인
    @PostMapping("/login")
    public Member login(@RequestBody MemberDTO memberDto) {

        // 콘솔창 확인용
        System.out.println("Login 시도 시 넘겨받은 데이터 : " + memberDto);

        // 넘겨받은 DTO에서 id값과 pw값만 뽑아서 id와 pw값만 가지고 있는 member객체 생성
        Member member = Member.builder()
                .userId(memberDto.getUserId())
                .userPw(memberDto.getUserPw())
                .build();

        // DB에서 id값과 pw값을 조회
        Member getMember = service.login(member);

        // 콘솔창 확인용
        System.out.println("DB에서 조회하여 받은 값 : " + getMember);

        if(getMember != null) {
            System.out.println("Member 객체 리턴");
            return getMember;
        }else{
            System.out.println("null값 리턴");
            return null;
        }

    }

    // id 중복 검사
    @PostMapping("/idCheck")
    public String idCheck(@RequestBody String id) {

        // 나중에..

        return "possible";
    }

    // 회원탈퇴
    @PostMapping("/delete")
    public void delete(@RequestBody MemberDTO memberDto) {

        // 데이터 통신 테스트
        System.out.println("회원탈퇴 시 넘겨받은 데이터값 : " + memberDto);


    }

}
