package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.DTO.ChangePasswordRequestDto;
import com.Library.SpringShelf.DTO.UserDto;
import com.Library.SpringShelf.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users") // A more descriptive base path
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')") // Only Admins or Librarians can call this
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')") // Only Admins or Librarians can call this
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> user = userService.getAllUser();
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/id")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')") // Only Admins or Librarians can call this
    public ResponseEntity<UserDto> UpdateUser(@PathVariable Long id,@RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(id,userDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto request,
            Authentication authentication) {

        String username = authentication.getName();
        userService.changePassword(username, request);

        // Return a simple success message
        Map<String, String> response = Map.of("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }
}