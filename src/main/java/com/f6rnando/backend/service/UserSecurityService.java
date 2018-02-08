package com.f6rnando.backend.service;

import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/************************************
 Created by f6rnando@gmail.com
 2018-02-08
 *************************************/

@Service
public class UserSecurityService implements UserDetailsService {

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(UserSecurityService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        return user;
    }
}
