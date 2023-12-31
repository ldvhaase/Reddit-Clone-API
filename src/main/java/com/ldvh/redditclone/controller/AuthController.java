package com.ldvh.redditclone.controller;

import com.ldvh.redditclone.dto.RegisterRequest;
import com.ldvh.redditclone.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Log4j2
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        log.info("HTTP request received: {}", registerRequest);
        authService.signup(registerRequest);
        return ResponseEntity.ok("Registration Successful");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("test successful");
    }
}
