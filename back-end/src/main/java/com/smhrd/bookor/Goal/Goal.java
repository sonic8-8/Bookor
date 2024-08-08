package com.smhrd.bookor.Goal;

import com.smhrd.bookor.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goals")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "user_goal", nullable = false)
    private String userGoal;

    @Column(name = "user_prgress", nullable = false)
    private String userPrgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

}