package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.Model.User;
import com.Library.SpringShelf.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")  // ✅ base path
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ constructor injection (preferred over field @Autowired)
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        System.out.println("controller");
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.login(user);
    }
}
