package com.f6rnando.test.integration;

import com.f6rnando.DevopsApplication;
import com.f6rnando.backend.persistence.domain.backend.Plan;
import com.f6rnando.backend.persistence.domain.backend.Role;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.persistence.domain.backend.UserRole;
import com.f6rnando.enums.PlansEnum;
import com.f6rnando.enums.RolesEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/************************************
 Created by f6rnando@gmail.com
 2018-02-07
 *************************************/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevopsApplication.class)
public class UserIntegrationTest extends AbstractIntegrationTest {
    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(UserIntegrationTest.class);

    @Rule
    public TestName testName = new TestName();

    @Before
    public void init() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception {
        Plan basicPlan = createPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Plan retrievedPlan = planRepository.findOne(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void testCreateNewRole() throws Exception {
        Role userRole = createRole(RolesEnum.BASIC);
        roleRepository.save(userRole);
        Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void testCreateUser() throws Exception {
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@f6rnando.com";
        User basicUser = createUser(username, email);

        basicUser = userRepository.save(basicUser);
        User newlyCreatedUser = userRepository.findOne(basicUser.getId());

        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.getPlan());
        Assert.assertNotNull(newlyCreatedUser.getPlan().getId());

        Set<UserRole> newlyUserRoles = newlyCreatedUser.getUserRoles();
        for (UserRole ur : newlyUserRoles) {
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }


        Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);
    }

    public void testDeleteUser() {
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@f6rnando.com";
        logger.debug("The username: {}", username);
        logger.debug("The email: {}", email);

        User basicUser = createUser(username, email);
        userRepository.delete(basicUser.getId());
    }

}
