package com.OnlineCanteen.AuthService.Utill;


import com.OnlineCanteen.AuthService.entity.User;
import com.OnlineCanteen.AuthService.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String secret;

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
    @Autowired
    private UserRepository userRepository;
    public String generateToken(String email) {
        User user = userRepository.findByEmail(email);
        return Jwts.builder()
                .setSubject(user.getEmail()).claim("role",user.getRole()).claim("name",user.getName()).claim("userId",user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey(),
                        SignatureAlgorithm.HS256)
                .compact();
    }
}
