package com.OnlineCanteen.AuthService.service;



import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    private static final String CLIENT_ID = "64355227379-s2254hgh8j58cg6p48v9qr12eqe6hsr3.apps.googleusercontent.com";

    public GoogleIdToken.Payload verify(String token) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);

        if (idToken == null) {
            throw new RuntimeException("Invalid Google Token");
        }

        return idToken.getPayload();
    }
}
