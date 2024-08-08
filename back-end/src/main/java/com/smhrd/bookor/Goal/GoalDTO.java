package com.smhrd.bookor.Goal;

import lombok.Data;

@Data
public class GoalDTO {
    private Long memberId;
    private String user_goal;
    private String user_prgress;
}