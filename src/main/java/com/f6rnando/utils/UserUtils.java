package com.f6rnando.utils;

import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.web.controllers.ForgotMyPasswordController;
import com.f6rnando.web.domain.frontend.BasicAccountPayload;

import javax.servlet.http.HttpServletRequest;

/************************************
 Created by f6rnando@gmail.com
 2018-02-08
 *************************************/

public class UserUtils {

    /** Non Instantiable */
    private UserUtils() {
        throw new AssertionError("Non instantiable");
    }

    /**
     * Creates a user with basic attributes set
     * @param username The site unique username
     * @param email The user's unique email
     * @return User entity
     */
    public static User createBasicUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(email);
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setPhoneNumber("+52-6647894561");
        user.setCountry("MX");
        user.setEnabled(true);
        user.setDescription("A basic user here");
        user.setProfileImageUrl("http:images.com/user/IUs25X28");

        return user;
    }

    /**
     * Creates a user with PRO attributes set
     * @return user
     */
    public static User createProUser() {
        User user = new User();
        user.setUsername("proUser");
        user.setPassword("secret");
        user.setEmail("pro@user.com");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setPhoneNumber("+52-6647894561");
        user.setCountry("MX");
        user.setEnabled(true);
        user.setDescription("A PRO user here");
        user.setProfileImageUrl("http:images.com/user/IUs25X28");

        return user;
    }

    /**
     * Builds an URL to reset the user password
     * @param request
     * @param userID
     * @param token
     * @return passwordRestURL
     */
    public static String createPasswordResetUrl(HttpServletRequest request, long userID, String token) {
        String passwordRestURL = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath() +
                ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userID + "&token=" + token;

        return passwordRestURL;
    }

    public static <T extends BasicAccountPayload> User fromWebUserToDomainUser(T frontendPayload) {
        User user = new User();
        user.setUsername(frontendPayload.getUsername());
        user.setPassword(frontendPayload.getPassword());
        user.setFirstName(frontendPayload.getFirstName());
        user.setLastName(frontendPayload.getLastName());
        user.setEmail(frontendPayload.getEmail());
        user.setPhoneNumber(frontendPayload.getPhoneNumber());
        user.setCountry(frontendPayload.getCountry());
        user.setEnabled(true);
        user.setDescription(frontendPayload.getDescription());

        return user;
    }
}
