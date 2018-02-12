package com.f6rnando;

import com.f6rnando.backend.persistence.domain.backend.Role;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.persistence.domain.backend.UserRole;
import com.f6rnando.backend.service.UserService;
import com.f6rnando.enums.PlansEnum;
import com.f6rnando.enums.RolesEnum;
import com.f6rnando.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DevopsApplication implements CommandLineRunner {

	// The application logger
	private static final Logger logger = LoggerFactory.getLogger(DevopsApplication.class);

	@Autowired
	private UserService userService;

	@Value("${webmaster.username}")
	private String webmasterUsername;

    @Value("${webmaster.password}")
	private String webmasterPassword;

    @Value("${webmaster.email}")
	private String webmasterEmail;

	public static void main(String[] args) {
		SpringApplication.run(DevopsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = UserUtils.createBasicUser(webmasterUsername, webmasterEmail);
		user.setPassword(webmasterPassword);
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, new Role(RolesEnum.ADMIN)));
		logger.debug("Creating a new user with username {}", user.getUsername());
		userService.createUser(user, PlansEnum.PRO, userRoles);
		logger.info("User {} created", user.getUsername());
	}
}
