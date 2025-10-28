package com.example.demo_api_movie.Test_Connection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/users/{id}")
    public String test() {
        return "Connection successfully";
    }
}
