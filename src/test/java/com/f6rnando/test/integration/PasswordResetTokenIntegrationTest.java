package com.f6rnando.test.integration;

import com.f6rnando.DevopsApplication;
import com.f6rnando.backend.persistence.domain.backend.PasswordResetToken;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.persistence.repositories.PasswordResetTokenRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/************************************
 Created by f6rnando@gmail.com
 2018-02-12
 *************************************/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevopsApplication.class)
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest {

    @Value("${token.expiration.minutes}")
    private int expirationTimeInMinutes;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void init() {
        Assert.assertFalse(expirationTimeInMinutes == 0);
    }

    @Test
    public void testTokenExpiration() throws Exception {
        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();

        LocalDateTime expectedTime = now.plusMinutes(expirationTimeInMinutes);
        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        LocalDateTime actualTime = passwordResetToken.getExpiryDate();
        Assert.assertNotNull(actualTime);
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test
    public void testFindTokenByTokenValue() throws Exception {
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        createPasswordResetToken(token, user, now);

        PasswordResetToken retrievedPasswordResetToken = passwordResetTokenRepository.findByToken(token);

        Assert.assertNotNull(retrievedPasswordResetToken);
        Assert.assertNotNull(retrievedPasswordResetToken.getId());
        Assert.assertNotNull(retrievedPasswordResetToken.getUser());
    }

    @Test
    public void testDeleteToken() throws Exception {
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        long tokenID = passwordResetToken.getId();
        passwordResetTokenRepository.delete(tokenID);
        PasswordResetToken shouldNotExistToken = passwordResetTokenRepository.findOne(tokenID);

        Assert.assertNull(shouldNotExistToken);
    }

    @Test
    public void testCascadeDeleteFromUserEntity() throws Exception {
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        long tokenID = passwordResetToken.getId();
        userRepository.delete(user.getId());

        Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findAllByUserId(user.getId());
        Assert.assertTrue(shouldBeEmpty.isEmpty());
    }

    public void testMultipleTokensAreReturnedWhenQueryingById() throws Exception {
        User user = createUser(testName);
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        Set<PasswordResetToken> tokens = new HashSet<>();

        tokens.add(createPasswordResetToken(token1, user, now));
        tokens.add(createPasswordResetToken(token2, user, now));
        tokens.add(createPasswordResetToken(token3, user, now));

        passwordResetTokenRepository.save(tokens);

        User foundUser = userRepository.findOne(user.getId());
        Set<PasswordResetToken> actualTokens = passwordResetTokenRepository.findAllByUserId(foundUser.getId());

        Assert.assertTrue(actualTokens.size() == tokens.size());
        List<String> tokenList = tokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
        List<String> actualTokenList = actualTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
        Assert.assertEquals(tokenList, actualTokenList);

    }

    /*      PRIVATE METHODS     */

    private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, expirationTimeInMinutes);
        passwordResetTokenRepository.save(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getId());

        return passwordResetToken;
    }
}
