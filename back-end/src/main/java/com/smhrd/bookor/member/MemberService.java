package com.smhrd.bookor.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository repository;

    public void join(Member member) {
        repository.save(member);
    }

    public Member login(Member member){
        return repository.findByUserIdAndUserPw(member.getUserId(), member.getUserPw());
    }


}
