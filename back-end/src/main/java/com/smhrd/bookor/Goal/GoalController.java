package com.smhrd.bookor.Goal;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GoalController {

    @PostMapping("/Goal")
    public String receiveGoal(@RequestBody GoalDTO goalDto) {

        // 받은 데이터 처리
        String user_goal = goalDto.getUser_goal();
        String user_prgress = goalDto.getUser_prgress();

        // 여기서 데이터를 처리하거나 저장할 수 있습니다.

        System.out.println(goalDto);

        return "user_goal: " + user_goal + ", user_prgress: " + user_prgress;
    }
}