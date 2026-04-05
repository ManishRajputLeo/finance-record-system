package com.finance.controller;

import com.finance.entity.Role;
import com.finance.entity.Status;
import com.finance.entity.User;
import com.finance.repository.UserRepository;
import com.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public User createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public User updateRole(@PathVariable Long id, @RequestParam Role role) {
        return userService.updateRole(id, role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public User updateStatus(@PathVariable Long id, @RequestParam Status status) {
        return userService.updateStatus(id, status);
    }
}
