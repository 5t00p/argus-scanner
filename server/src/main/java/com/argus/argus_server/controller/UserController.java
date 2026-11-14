package com.argus.argus_server.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argus.argus_server.model.AppUser;
import com.argus.argus_server.repository.AppUserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AppUserRepository userRepository;

    public UserController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }
    
     // Retrieve a user by ID
    @GetMapping("/{id}")
    public AppUser getUser(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}