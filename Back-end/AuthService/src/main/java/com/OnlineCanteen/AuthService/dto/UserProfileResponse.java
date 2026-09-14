package com.OnlineCanteen.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {

    private long userId;
    private String name;
    private String email;
    private String role;
}
