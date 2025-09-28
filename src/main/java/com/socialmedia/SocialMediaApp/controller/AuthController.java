package com.socialmedia.SocialMediaApp.controller;

import com.socialmedia.SocialMediaApp.auth.JwtProvider;
import com.socialmedia.SocialMediaApp.entities.User;
import com.socialmedia.SocialMediaApp.httpEntities.JwtResponse;
import com.socialmedia.SocialMediaApp.httpEntities.LoginRequest;
import com.socialmedia.SocialMediaApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User savedUser = userService.createUser(registerRequest);

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(true,jwt,userService.getUserByUsername(userDetails.getUsername())));
    }
}