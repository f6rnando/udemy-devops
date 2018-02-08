package com.f6rnando.utils;

import com.f6rnando.backend.persistence.domain.backend.User;

/************************************
 Created by f6rnando@gmail.com
 2018-02-08
 *************************************/

public class UsersUtils {

    /** Non Instantiable */
    private UsersUtils() {
        throw new AssertionError("Non instantiable");
    }

    /**
     * Creates a user with basic attributes set
     * @return
     */
    public static User createBasicUser() {
        User user = new User();
        user.setUsername("basicUser");
        user.setPassword("secret");
        user.setEmail("basic@user.com");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setPhoneNumber("+52-6647894561");
        user.setCountry("MX");
        user.setEnabled(true);
        user.setDescription("A basic user here");
        user.setProfileImageUrl("http:images.com/user/IUs25X28");

        return user;
    }
}
