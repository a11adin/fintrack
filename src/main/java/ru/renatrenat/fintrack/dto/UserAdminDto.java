package ru.renatrenat.fintrack.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ru.renatrenat.fintrack.model.User}
 */
@Value
public class UserAdminDto implements Serializable {
    long id;
    String username;
    String email;
    String password;
}