package com.smhrd.bookor.Goal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping("/goal")
    public ResponseEntity<String> receiveGoal(@RequestBody GoalDTO goalDto) {
        Long memberId = goalDto.getMemberId();
        System.out.println(memberId);
        if (memberId == null) {
            return new ResponseEntity<>("Member ID must not be null", HttpStatus.BAD_REQUEST);
        }


        Goal goal = Goal.builder()
                .userGoal(goalDto.getUser_goal())
                .userPrgress(goalDto.getUser_prgress())
                .build();

        Goal saveGoal = goalService.saveOrUpdateGoal(memberId, goal);

        String responseMessage = String.format("Goal created or updated with ID: %d, user_goal: %s, user_progress: %s",
                saveGoal.getGoalId(), saveGoal.getUserGoal(), saveGoal.getUserPrgress());

        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }
}