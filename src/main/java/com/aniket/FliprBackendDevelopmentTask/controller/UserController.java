package com.aniket.FliprBackendDevelopmentTask.controller;


import com.aniket.FliprBackendDevelopmentTask.model.User;
import com.aniket.FliprBackendDevelopmentTask.service.JwtService;
import com.aniket.FliprBackendDevelopmentTask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    private UserService service;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println("Encrypted using BCrypt Password : " + user.getPassword());

        return ResponseEntity.ok(userService.signup(user));
    }

    @PostMapping("/signin")
    public String signin(@RequestBody User user){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));

        if(authentication.isAuthenticated())    return jwtService.generateToken(user.getEmail());//"Login Successful..!";
        else    return "Login Failed...";
    }
}
