package com.tourism.api.controller;

import com.tourism.api.security.JwtTokenUtils;
import com.tourism.model.dto.TokenResponse;
import com.tourism.model.dto.CustomerDto;
import com.tourism.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AuthController {

    private final CustomerService customerService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(CustomerService customerService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public TokenResponse createAuthToken(@RequestBody CustomerDto customerDto) {
        UserDetails userDetails = customerService.loadUserByUsername(customerDto.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), customerDto.getPassword()));
        String token = jwtTokenUtils.generateToken(userDetails);
        return new TokenResponse(200, token);
    }

    @GetMapping("/profile/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear the token from client side (usually in frontend)
        SecurityContextHolder.clearContext(); // Clear authentication from security context
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

}
