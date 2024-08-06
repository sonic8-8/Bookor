package com.smhrd.bookor.Goal;


import com.smhrd.bookor.HelloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final HelloRepository helloRepository;

}
