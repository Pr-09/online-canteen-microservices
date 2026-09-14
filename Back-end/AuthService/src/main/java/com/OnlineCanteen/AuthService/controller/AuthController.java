package com.OnlineCanteen.AuthService.controller;

import com.OnlineCanteen.AuthService.dto.UserProfileResponse;
import com.OnlineCanteen.AuthService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.OnlineCanteen.AuthService.dto.LoginResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private AuthService service;

    @GetMapping("/me")
    public UserProfileResponse getCurrentUser(
            @RequestHeader("x-user-id") long userId,
            @RequestHeader("x-user-name") String name,
            @RequestHeader("x-user-email") String email,
            @RequestHeader("x-user-role") String role
    ) {
        return new UserProfileResponse(userId, name, email, role);
    }
    @GetMapping("/checkApi")
    public String check(){
        return "Ok";
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody Map<String, String> req) throws Exception {
//        String jwt = service.googleLogin(req.get("token"));
//        LoginResponse response = new LoginResponse();
        LoginResponse response=service.googleLogin(req.get("token"));
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/google")
//    public Map<String, String> googleLogin(@RequestBody Map<String, String> req) throws Exception {
//        String jwt = service.googleLogin(req.get("token"));
//        return Map.of("token", jwt);
//    }
}
