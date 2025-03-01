package com.mariia.task_manager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/profile")
    public UserDetails getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("UserDetails: " + userDetails);
        return userDetails;
    }
}