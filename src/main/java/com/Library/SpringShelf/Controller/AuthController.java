package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.Dto.UserDto;
import com.Library.SpringShelf.Dto.UserLoginDTO;
import com.Library.SpringShelf.Dto.UserRegistrationDto;
import com.Library.SpringShelf.Service.TokenBlacklistService;
import com.Library.SpringShelf.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the MEMBER role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided (e.g., email already exists, validation error)")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserDto createdUser = userService.register(registrationDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate a user", description = "Authenticates a user with email and password, and returns a JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO);
        Map<String, String> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout a user", description = "Logout a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(jwt);
        }
        return ResponseEntity.ok("Successfully logged out");
    }
}