package com.mariia.task_manager.controller;

import com.mariia.task_manager.model.Role;
import com.mariia.task_manager.model.User;
import com.mariia.task_manager.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/promote/{username}")
    public String promoteToAdmin(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return "User not found!";
        }

        User user = userOptional.get();
        user.setRole(Role.ADMIN);  // Promote to ADMIN
        userRepository.save(user);

        return "User " + username + " has been promoted to ADMIN!";
    }
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Welcome, Admin! You have access to this dashboard.";
    }
}
