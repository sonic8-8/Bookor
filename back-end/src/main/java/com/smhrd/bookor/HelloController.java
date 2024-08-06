package com.smhrd.bookor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("/api")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {

        Map<String, String> data = new HashMap<>();
        data.put("응답", "Hello World");

        System.out.println(data);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/save")
    public void save(@RequestBody Map<String, String> data) {



    }
}
