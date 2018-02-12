package com.f6rnando.test.integration;

import com.f6rnando.DevopsApplication;
import com.f6rnando.backend.persistence.domain.backend.Role;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.persistence.domain.backend.UserRole;
import com.f6rnando.backend.service.UserSecurityService;
import com.f6rnando.backend.service.UserService;
import com.f6rnando.enums.PlansEnum;
import com.f6rnando.enums.RolesEnum;
import com.f6rnando.utils.UserUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

/************************************
 Created by f6rnando@gmail.com
 2018-02-08
 *************************************/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevopsApplication.class)
public class UserServiceIntegrationTest {

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(UserServiceIntegrationTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Rule
    public TestName testName = new TestName();

    @Test
    public void testCreateNewUser() throws Exception {
        Set<UserRole> userRoles = new HashSet<>();
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@f6rnando.com";
        logger.debug("The username: {}", username);
        logger.debug("The email: {}", email);
        User basicUser = UserUtils.createBasicUser(username, email);
        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
        User user = userService.createUser(basicUser, PlansEnum.BASIC, userRoles);

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        User proUser = UserUtils.createProUser();
        UserDetails userDetails = null;

        try {
            userDetails = userSecurityService.loadUserByUsername(proUser.getUsername());
        } catch (UsernameNotFoundException e) {
            logger.debug("Username not found");
        }

        Assert.assertNull(userDetails);

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(proUser, new Role(RolesEnum.PRO)));
        proUser = userService.createUser(proUser, PlansEnum.PRO, userRoles);

        try {
            userDetails = userSecurityService.loadUserByUsername(proUser.getUsername());
        } catch (UsernameNotFoundException e) {
            logger.debug("Username not found");
        }

        Assert.assertNotNull(userDetails);
    }
}
