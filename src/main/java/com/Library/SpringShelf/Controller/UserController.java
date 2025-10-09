package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.Dto.ChangePasswordRequestDto;
import com.Library.SpringShelf.Dto.UserDto;
import com.Library.SpringShelf.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get a user by their ID", description = "Returns details for a single user. Requires ADMIN or LIBRARIAN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')") // Only Admins or Librarians can call this
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all user details", description = "Retrieves the details of all user. Requires ADMIN or LIBRARIAN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all the user"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')") // Only Admins or Librarians can call this
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> user = userService.getAllUser();
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user details by ID", description = "Update the details for a specific user. Requires ADMIN or LIBRARIAN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')") // Only Admins or Librarians can call this
    public ResponseEntity<UserDto> UpdateUser(@PathVariable Long id,@RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(id,userDto);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Delete user details by ID", description = "Delete the details for a specific user. Requires ADMIN or LIBRARIAN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change the Password of the user", description = "Change the Password for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID")
    })
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto request,
            Authentication authentication) {

        String username = authentication.getName();
        userService.changePassword(username, request);


        Map<String, String> response = Map.of("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }
}