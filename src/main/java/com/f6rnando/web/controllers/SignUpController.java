package com.f6rnando.web.controllers;

import com.f6rnando.backend.persistence.domain.backend.Plan;
import com.f6rnando.backend.persistence.domain.backend.Role;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.persistence.domain.backend.UserRole;
import com.f6rnando.backend.service.PlanService;
import com.f6rnando.backend.service.UserService;
import com.f6rnando.config.CountriesConfig;
import com.f6rnando.enums.PlansEnum;
import com.f6rnando.enums.RolesEnum;
import com.f6rnando.utils.UserUtils;
import com.f6rnando.web.domain.frontend.BasicAccountPayload;
import com.f6rnando.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;

/************************************
 Created by f6rnando@gmail.com
 2018-02-16
 *************************************/

@Controller
public class SignUpController {

    /*      FIELDS      */

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    public static final String SIGNUP_URL_MAPPING = "/signup";

    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";

    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";

    public static final String DUPLICATED_USERNAME = "duplicatedUsername";

    public static final String DUPLICATED_EMAIL = "duplicatedEmail";

    public static final String SIGNED_UP_MESSAGE = "signedUp";

    public static final String ERROR_MESSAGE = "message";

    @Autowired
    private CountriesConfig countriesConfig;

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;

    /*      METHODS      */

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signUpGet(@RequestParam("planID") int planID, ModelMap model) {

        if (planID != PlansEnum.BASIC.getId() && planID != PlansEnum.PRO.getId()) {
            throw new IllegalArgumentException("The Plan ID " + planID + "is not valid");
        }

        // Sorting the Map
        Map<String, String> countryMap = new TreeMap<>(countriesConfig.getKey());

        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());
        model.addAttribute("countryMap", countryMap);

        return SUBSCRIPTION_VIEW_NAME;
    }

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.POST)
    public String signUpPost(@RequestParam(name = "planID") int planID,
                             @RequestParam(name = "file", required = false) MultipartFile file,
                             @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload,
                             ModelMap model) {
        // Sorting the Map
        Map<String, String> countryMap = new TreeMap<>(countriesConfig.getKey());
        model.addAttribute("countryMap", countryMap);

        if (planID != PlansEnum.BASIC.getId() && planID != PlansEnum.PRO.getId()) {
            model.addAttribute(SIGNED_UP_MESSAGE, "false");
            model.addAttribute(ERROR_MESSAGE, "Plan ID does not exist");
        } else {
            this.checkForDuplicated(payload, model);
            boolean duplicates = false;
            List<String> errorMessages = new ArrayList<>();

            if (model.containsKey(DUPLICATED_USERNAME)) {
                logger.warn("The Username already exist. Displaying error to the user");
                model.addAttribute(SIGNED_UP_MESSAGE, "false");
                errorMessages.add("Username already exist");
                duplicates = true;
            }

            if (model.containsKey(DUPLICATED_EMAIL)) {
                logger.warn("The Email already exist. Displaying error to the user");
                model.addAttribute(SIGNED_UP_MESSAGE, "false");
                errorMessages.add("Email already exist");
                duplicates = true;
            }

            if (duplicates) {
                model.addAttribute(ERROR_MESSAGE, errorMessages);
            } else {
                logger.debug("Transforming user payload into User domain object");
                User user = UserUtils.fromWebUserToDomainUser(payload);

                // Stores the profile image on Amazon S3 and stores the URL in the user's db record
                if (file != null && !file.isEmpty()) {
                    String profileImageUrl = null;
                    if (profileImageUrl != null) {
                        user.setProfileImageUrl(profileImageUrl);
                    } else {
                        logger.warn("There was a problem uploading the profile image to S3.");
                        logger.warn("The user's profile will be created without the image");
                    }
                }

                logger.debug("Retrieving plan from the database");
                Plan selectedPlan = planService.findPlanById(planID);

                if (selectedPlan == null) {
                    logger.error("The plan ID {} could not be found. Throwing exception.", planID);
                    model.addAttribute(SIGNED_UP_MESSAGE, "false");
                    model.addAttribute(ERROR_MESSAGE, "Plan ID not found");
                } else {
                    user.setPlan(selectedPlan);
                    User registeredUser = null;

                    // By default users get the BaSIC ROLE
                    Set<UserRole> roles = new HashSet<>();

                    if (planID == PlansEnum.BASIC.getId()) {
                        roles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
                        registeredUser = userService.createUser(user, PlansEnum.BASIC, roles);
                    } else {
                        roles.add(new UserRole(user, new Role(RolesEnum.PRO)));
                        registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
                    }

                    // Auto-login the registered user
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            registeredUser, null, registeredUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    logger.info("User created successfully");

                    model.addAttribute(SIGNED_UP_MESSAGE, "true");
                }
            }
        }

        return SUBSCRIPTION_VIEW_NAME;
    }

    /**
     * Checks if the username/email are duplicates and sets the error flags in the model
     * @param payload
     * @param model
     */
    private void checkForDuplicated(BasicAccountPayload payload, ModelMap model) {

        if (userService.findByUsername(payload.getUsername()) != null) {
            model.addAttribute(DUPLICATED_USERNAME, true);
        }

        if (userService.findByEmail(payload.getEmail()) != null) {
            model.addAttribute(DUPLICATED_EMAIL, true);
        }
    }
}
