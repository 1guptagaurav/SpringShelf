package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.DTO.UserLoginDTO;
import com.Library.SpringShelf.DTO.UserRegistrationDto;
import com.Library.SpringShelf.Model.Role;
import com.Library.SpringShelf.Model.Rolename;
import com.Library.SpringShelf.Model.User;
import com.Library.SpringShelf.Model.UserDto;
import com.Library.SpringShelf.Repository.RoleRepository;
import com.Library.SpringShelf.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public User register(UserRegistrationDto userRegistrationDto) {
        User user=new User();
        user.setFirstname(userRegistrationDto.getFirstname());
        if (userRegistrationDto.getLastname() == null || userRegistrationDto.getLastname().isBlank()) {
            user.setLastname("LNU");
        } else {
            user.setLastname(userRegistrationDto.getLastname());
        }
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(encoder.encode(userRegistrationDto.getPassword()));
        Role memberRole = roleRepository.findByRole(Rolename.MEMBER)
                .orElseThrow(() -> new RuntimeException("Error: Default role MEMBER is not found."));
        user.setRoles(Set.of(memberRole));
        return userRepository.save(user);
    }

    public String login(@Valid UserLoginDTO userLoginDTO) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userLoginDTO.getEmail());
        } else {
            return "fail";
        }
    }
    private UserDto ConvertToUserDto(User user){
        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.toSet());
        userDto.setRoles(roleNames);
        return userDto;
    }
    public UserDto getUserById(Long id) {
        User user=userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not found"));
        return ConvertToUserDto(user);
    }
}
