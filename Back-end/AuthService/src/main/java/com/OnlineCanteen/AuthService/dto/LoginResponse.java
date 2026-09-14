package com.OnlineCanteen.AuthService.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String name;
    private String email;
    private String role;
}
