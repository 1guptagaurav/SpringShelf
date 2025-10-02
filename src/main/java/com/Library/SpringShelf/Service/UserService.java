package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.DTO.ChangePasswordRequestDto;
import com.Library.SpringShelf.DTO.UserLoginDTO;
import com.Library.SpringShelf.DTO.UserRegistrationDto;
import com.Library.SpringShelf.Exception.ResourceNotFoundException;
import com.Library.SpringShelf.Model.Role;
import com.Library.SpringShelf.Model.Rolename;
import com.Library.SpringShelf.Model.User;
import com.Library.SpringShelf.DTO.UserDto;
import com.Library.SpringShelf.Repository.BorrowingTransactionRepository;
import com.Library.SpringShelf.Repository.RoleRepository;
import com.Library.SpringShelf.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // Inject the PasswordEncoder bean from SecurityConfig
    private final BorrowingTransactionRepository transactionRepository;
    public UserDto register(UserRegistrationDto userRegistrationDto) {
        String email = userRegistrationDto.getEmail().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("An account with this email already exists: " + email);
        }
        User user = new User();
        user.setFirstname(userRegistrationDto.getFirstname());
        user.setLastname(userRegistrationDto.getLastname() == null || userRegistrationDto.getLastname().isBlank() ? "LNU" : userRegistrationDto.getLastname());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        Role memberRole = roleRepository.findByRole(Rolename.MEMBER)
                .orElseThrow(() -> new RuntimeException("Error: Default role MEMBER is not found."));
        user.setRoles(Set.of(memberRole));

        User savedUser = userRepository.save(user);
        return convertToUserDto(savedUser);
    }

    public String login(UserLoginDTO userLoginDTO) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail().toLowerCase(), userLoginDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userLoginDTO.getEmail());
        }
        throw new RuntimeException("Authentication failed");
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToUserDto(user);
    }

    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
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

    public List<UserDto> getAllUser() {
        List<User> user=userRepository.findAll();
        return user.stream()
                .map(this::convertToUserDto)
                .toList();
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (userDto.getFirstname() != null) {
            user.setFirstname(userDto.getFirstname());
        }
        if (userDto.getLastname() != null) {
            user.setLastname(userDto.getLastname());
        }else{
            user.setLastname("LNU");
        }

        if (userDto.getEmail() != null) {
            String newEmail = userDto.getEmail().toLowerCase();
            if (!newEmail.equals(user.getEmail())) {
                if (userRepository.findByEmail(newEmail).isPresent()) {
                    throw new RuntimeException("Email " + newEmail + " is already in use by another account.");
                }
                System.out.println("Logic to send verification email to " + user.getEmail() + " would go here.");
                user.setEmail(newEmail);
            }
        }
        return convertToUserDto(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        transactionRepository.deleteAllByBorrower(user);
        userRepository.delete(user);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequestDto request) {
        // 1. Get the currently authenticated user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Check if the new password and confirmation match
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new RuntimeException("New password and confirmation password do not match.");
        }

        // 3. Verify the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect current password.");
        }

        // 4. If all checks pass, encode the new password and update the user
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // No need to call .save() because of @Transactional
    }

}