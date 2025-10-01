package com.Library.SpringShelf.Model;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Set<String> roles;
}
