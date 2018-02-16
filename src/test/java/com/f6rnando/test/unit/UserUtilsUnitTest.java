package com.f6rnando.test.unit;

import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.utils.UserUtils;
import com.f6rnando.web.controllers.ForgotMyPasswordController;
import com.f6rnando.web.domain.frontend.BasicAccountPayload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.UUID;

/************************************
 Created by f6rnando@gmail.com
 2018-02-13
 *************************************/

public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    private PodamFactory podamFactory;

    @Before
    public void init() {
        mockHttpServletRequest = new MockHttpServletRequest();
        podamFactory = new PodamFactoryImpl();
    }

    @Test
    public void testPasswordResetEmailUrlConstruction() {
        mockHttpServletRequest.setServerPort(8080);
        String token = UUID.randomUUID().toString();
        long userID = 123456;

        String expectedURL = "http://localhost:8080" + ForgotMyPasswordController.CHANGE_PASSWORD_PATH +
                "?id=" + userID + "&token=" + token;

        String actualURL = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userID,token);

        Assert.assertEquals(expectedURL, actualURL);
    }

    @Test
    public void mapWebUserToDomainUser() {
        BasicAccountPayload webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayload.class);
        webUser.setEmail("me@example.com");

        User user = UserUtils.fromWebUserToDomainUser(webUser);
        Assert.assertNotNull(user);

        Assert.assertEquals(webUser.getUsername(), user.getUsername());
        Assert.assertEquals(webUser.getPassword(), user.getPassword());
        Assert.assertEquals(webUser.getFirstName(), user.getFirstName());
        Assert.assertEquals(webUser.getLastName(), user.getLastName());
        Assert.assertEquals(webUser.getEmail(), user.getEmail());
        Assert.assertEquals(webUser.getPhoneNumber(), user.getPhoneNumber());
        Assert.assertEquals(webUser.getCountry(), user.getCountry());
        Assert.assertEquals(webUser.getDescription(), user.getDescription());
    }
}
