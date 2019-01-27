package dev.posadskiy.skillrepeat.dto;

import lombok.Data;

@Data
public class Auth {
    private String login;
    private String password;
    private String email;
}