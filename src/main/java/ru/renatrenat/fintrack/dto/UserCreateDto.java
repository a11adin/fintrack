package ru.renatrenat.fintrack.dto;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.renatrenat.fintrack.model.Role;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link ru.renatrenat.fintrack.model.User}
 */
@Value
@NoArgsConstructor(force = true)
public class UserCreateDto implements Serializable {
    @NotNull(message = "Username can not be empty")
    String username;
    @NotNull
    String email;
    @NotNull
    String password;
    Set<Role> roles;
}