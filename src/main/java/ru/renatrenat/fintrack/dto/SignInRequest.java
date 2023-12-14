package ru.renatrenat.fintrack.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SignInRequest {
    private String username;
    private String password;
}
