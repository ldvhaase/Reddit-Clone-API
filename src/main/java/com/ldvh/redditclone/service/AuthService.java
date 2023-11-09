package com.ldvh.redditclone.service;

import com.ldvh.redditclone.dto.RegisterRequest;
import com.ldvh.redditclone.model.NotificationEmail;
import com.ldvh.redditclone.model.User;
import com.ldvh.redditclone.model.VerificationToken;
import com.ldvh.redditclone.repository.UserRepo;
import com.ldvh.redditclone.repository.VerificationTokenRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class AuthService {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final VerificationTokenRepo verificationTokenRepo;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepo.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please confirm your account below",
                user.getEmail(),
                "Thank you for signing up for RedditClone. Please click on the link to activate your account:" +
                        "http://localhost:8080/api/auth/accountVerification" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        log.info("Verification token needed: {}", verificationToken);

        verificationTokenRepo.save(verificationToken);
        return token;
    }
}
