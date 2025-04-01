package com.hask.hasktask.controller;

import com.hask.hasktask.model.User;
import com.hask.hasktask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CacheConfig(cacheNames = "users")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Cacheable("users")
    @GetMapping
    /*@ParameterObject: For SpringDoc OpenAPI Swagger-UI*/
    public ResponseEntity<List<User>> getAll(@ParameterObject Pageable pageable) {

        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getLoggedInUser(Principal loggedInUser) {

        return ResponseEntity.ok(userService.getLoggedInUser(loggedInUser));
    }

    /*
     * GET User details by user Email or Phone*/
    @GetMapping("/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {

        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable String userId) {

        return ResponseEntity.ok(userService.getByEmail(userId));
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody User request) {
        userService.update(request);
        return ResponseEntity.noContent().build();
    }

    /*
     * DELETE User by ID OR userEmail*/
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId) {

        userService.deleteByIdOrEmail(userId);

        return ResponseEntity.noContent().build();
    }
}
