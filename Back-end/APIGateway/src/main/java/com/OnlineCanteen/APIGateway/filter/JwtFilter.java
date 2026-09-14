package com.OnlineCanteen.APIGateway.filter;

import com.OnlineCanteen.APIGateway.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Public APIs
            if (path.startsWith("/api/auth/login")|| path.startsWith("/api/auth/checkApi") ||path.startsWith("/api/auth/google") || path.startsWith("/api/menu") ) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            String token =
                    authHeader.substring(7);

            try {

                Claims claims = jwtUtil.getClaims(token);

                String role = claims.get("role", String.class);

                String email = claims.getSubject();
                String name= claims.get("name", String.class);

                Number userIdClaim = claims.get("userId", Number.class);

                if (userIdClaim == null) {
                    throw new RuntimeException("userId not present in JWT");
                }

                Long userId = userIdClaim.longValue();

                // Admin APIs are only accessible to admin
                if (path.startsWith("/api/admin") && !"ADMIN".equals(role)) {

                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);

                    return exchange.getResponse().setComplete();
                }

                exchange = exchange.mutate().request(
                                        exchange.getRequest()
                                                .mutate()
                                                .header("x-user-id", userId.toString())
                                                .header("x-user-email", email)
                                                .header("x-user-role", role )
                                                .header("x-user-name",name)
                                                .build()
                                ).build();

            } catch (Exception e) {

                System.out.println("JWT VALIDATION FAILED: " + e.getMessage());

                e.printStackTrace();

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }
}
