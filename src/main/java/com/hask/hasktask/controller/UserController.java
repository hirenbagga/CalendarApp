package com.hask.hasktask.controller;

import com.hask.hasktask.model.ChangePasswordRequest;
import com.hask.hasktask.model.User;
import com.hask.hasktask.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Management")
public class UserController {
    /*
     * Security Flow: The Frontend (UI) sends an AccessToken with the request.
     * If the AccessToken has expired, the Backend responds with a 401 Unauthorized error.
     * The Frontend Interceptor then detects the 401 response,
     * triggers a request to refresh the token,
     * and retrieves a new AccessToken from the Backend using the RefreshToken.
     */

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

    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changeUserPassword(request);
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
