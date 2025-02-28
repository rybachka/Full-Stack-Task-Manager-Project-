package com.mariia.task_manager.repository;

import com.mariia.task_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository; // This gives basic CRUD operations without writing SQL


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
