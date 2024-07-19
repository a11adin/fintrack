package ru.renatrenat.fintrack.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.renatrenat.fintrack.model.ERole;
import ru.renatrenat.fintrack.model.Role;
import ru.renatrenat.fintrack.model.User;
import ru.renatrenat.fintrack.repository.RoleRepository;
import ru.renatrenat.fintrack.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@DependsOn("flyway")
public class AdminInitializer implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Value(value = "${fintrack.app.admin}")
    private String username;

    @Value(value="${fintrack.app.adminpassword}")
    String password;


    public AdminInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, Flyway flyway) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.existsByUsername(username)){
            Set<Role> roles = new HashSet<>();
            Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_ADMIN);
            roles.add(userRole.get());
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail("admin@renatik.com");
            user.setRoles(roles);
            userRepository.save(user);
            System.out.println("Admin created");
        }

    }
}
