package com.OnlineCanteen.AuthService.service;




import com.OnlineCanteen.AuthService.Utill.JwtUtil;
import com.OnlineCanteen.AuthService.dto.LoginResponse;
import com.OnlineCanteen.AuthService.entity.User;
import com.OnlineCanteen.AuthService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private GoogleAuthService googleService;

    @Autowired
    private UserRepository repo;



    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse googleLogin(String token) throws Exception {

        var payload = googleService.verify(token);

        String email = payload.getEmail();
        String name = (String) payload.get( "name");
//        String MobileNo=(String) payload.ge
        User user = repo.findByEmail(email);
        if(user==null){
            User u = new User();
            u.setEmail(email);
            u.setName(name);
            u.setRole("USER");
            u.setPassword(null); // important for Google users
            repo.save(u);
        }
        LoginResponse response=new LoginResponse();

        String jwtString= jwtUtil.generateToken(email);

        response.setToken(jwtString);
        response.setEmail(email);
        response.setName(name);
        response.setRole("USER");

        return response;

    }

//    public String googleLogin(String token) throws Exception {
//
//        var payload = googleService.verify(token);
//
//        String email = payload.getEmail();
//        String name = (String) payload.get( "name");
////        String MobileNo=(String) payload.ge
//        User user = repo.findByEmail(email);
//        if(user==null){
//            User u = new User();
//            u.setEmail(email);
//            u.setName(name);
//            u.setRole("USER");
//            u.setPassword(null); // important for Google users
//            repo.save(u);
//        }
//
//        return jwtUtil.generateToken(email);
//    }
}
