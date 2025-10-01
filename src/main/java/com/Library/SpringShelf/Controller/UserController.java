package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.DTO.UserLoginDTO;
import com.Library.SpringShelf.DTO.UserRegistrationDto;
import com.Library.SpringShelf.Model.User;
import com.Library.SpringShelf.Model.UserDto;
import com.Library.SpringShelf.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // constructor injection (preferred over field @Autowired)
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserRegistrationDto user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        return userService.login(userLoginDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')") // Only Admins or Librarians can call this
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        // This will return the decrypted user details, but only if the caller is authorized
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
