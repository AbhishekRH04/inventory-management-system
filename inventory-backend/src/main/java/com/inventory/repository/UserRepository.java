package com.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);
}