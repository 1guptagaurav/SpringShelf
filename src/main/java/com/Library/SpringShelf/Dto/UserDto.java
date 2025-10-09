package com.Library.SpringShelf.DTO;

import com.Library.SpringShelf.Utils.EmailMaskingSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;

    @JsonSerialize(using = EmailMaskingSerializer.class)
    private String email;

    private Set<String> roles;
}
