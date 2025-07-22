package com.sharp.sharpshap.controller;


import com.sharp.sharpshap.dto.UserDTO;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.error.ErrorResponse;
import com.sharp.sharpshap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO dtoUser, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body("Объект не валиден" + errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(dtoUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("/all-users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable UUID id,
                                             @Valid @RequestBody User user,
                                             BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errors = result.getFieldErrors().stream()
                        .map(error -> error.getField() + ":" + error.getDefaultMessage())
                        .collect(Collectors.joining(","));
                return ResponseEntity.badRequest().body("Объект не валиден" + errors);
            }
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (RuntimeException e) {
            return ErrorResponse.error(new UsernameNotFoundException(""),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Object> getAuthenticationUser() {
        return ResponseEntity.ok().body(userService.getCurrentUserName());
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchByNameOrLastName(@RequestParam String prefix) {
        List<User> users = userService.findUsersByFirstNameOrLastName(prefix);
        return ResponseEntity.ok().body(users);
    }
}
