package ru.renatrenat.fintrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.renatrenat.fintrack.dto.JwtResponse;
import ru.renatrenat.fintrack.dto.SignInRequest;
import ru.renatrenat.fintrack.dto.SignUpRequest;
import ru.renatrenat.fintrack.model.ERole;
import ru.renatrenat.fintrack.model.Role;
import ru.renatrenat.fintrack.model.User;
import ru.renatrenat.fintrack.repository.RoleRepository;
import ru.renatrenat.fintrack.repository.UserRepository;
import ru.renatrenat.fintrack.service.UserDetailsImpl;
import ru.renatrenat.fintrack.util.JwtUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles =userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setId(userDetails.getId());
        res.setUsername(userDetails.getUsername());
        res.setRoles(roles);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest){
        if (userRepository.existsByUsername(signUpRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken");
        }

        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
        if (userRole.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Role not found");
        }
        roles.add(userRole.get());
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(hashedPassword);
        user.setEmail(signUpRequest.getEmail());
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

}
