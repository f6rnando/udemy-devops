package com.f6rnando.backend.service;

import com.f6rnando.backend.persistence.domain.backend.PasswordResetToken;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.persistence.repositories.PasswordResetTokenRepository;
import com.f6rnando.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/************************************
 Created by f6rnando@gmail.com
 2018-02-12
 *************************************/

@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService {

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${token.expiration.minutes}")
    private int tokenExpirationMinutes;

    /**
     * Retrieves a Password Reset Token for the given token ID
     * @param token The token to be returned
     * @return A PRT/Null
     */
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public PasswordResetToken createPasswordResetTokenByEmail(String email) {
        PasswordResetToken passwordResetToken = null;
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            passwordResetToken = new PasswordResetToken(token, user, now, tokenExpirationMinutes);
            passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);

            logger.debug("Token {} created successfully for user {}", token, user.getUsername());
        } else {
            logger.warn("The user with email '{}' was not found", email);
        }

        return passwordResetToken;
    }
}
