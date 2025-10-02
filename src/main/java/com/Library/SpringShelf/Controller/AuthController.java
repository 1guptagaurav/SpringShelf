package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.DTO.UserDto;
import com.Library.SpringShelf.DTO.UserLoginDTO;
import com.Library.SpringShelf.DTO.UserRegistrationDto;
import com.Library.SpringShelf.Service.TokenBlacklistService;
import com.Library.SpringShelf.Service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserDto createdUser = userService.register(registrationDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO);
        // Return a proper JSON object, e.g., {"token": "..."}
        Map<String, String> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Extract the token from the Authorization header
        final String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(jwt);
        }

        return ResponseEntity.ok("Successfully logged out");
    }
}