package com.smhrd.bookor.Goal;


import com.smhrd.bookor.member.Member;
import com.smhrd.bookor.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;

    @Transactional
    public Goal saveOrUpdateGoal(Long memberId, Goal goal) {
        // Member 엔티티를 먼저 로드 또는 저장
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // Goal 엔티티에 Member 설정
        goal.setMember(member);

        // Goal 엔티티 저장
        Optional<Goal> existingGoal = goalRepository.findByMemberId(memberId);
        if (existingGoal.isPresent()) {
            Goal updatedGoal = existingGoal.get();
            updatedGoal.setUserGoal(goal.getUserGoal());
            updatedGoal.setUserPrgress(goal.getUserPrgress());
            return goalRepository.save(updatedGoal);
        } else {
            return goalRepository.save(goal);
        }
    }

    public List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    public Optional<Goal> getGoalById(Long id) {
        return goalRepository.findById(id);
    }

    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}