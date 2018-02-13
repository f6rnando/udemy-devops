package com.f6rnando.test.unit;

import com.f6rnando.utils.UserUtils;
import com.f6rnando.web.controllers.ForgotMyPasswordController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.UUID;

/************************************
 Created by f6rnando@gmail.com
 2018-02-13
 *************************************/

public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    @Before
    public void init() {
        mockHttpServletRequest = new MockHttpServletRequest();
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
}
