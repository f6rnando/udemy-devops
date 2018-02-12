package com.f6rnando.backend.persistence.domain.backend;

import com.f6rnando.backend.persistence.converters.LocalDateTimeAttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/************************************
 Created by f6rnando@gmail.com
 2018-02-12
 *************************************/

@Entity
public class PasswordResetToken implements Serializable {

    /*      FIELDS      */

    private static final long serialVersionUID = 1L;

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetToken.class);

    private static final int DEFAULT_TOKEN_MINUTES = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expiryDate;

    /*      METHODS     */

    public PasswordResetToken() {
    }

    public PasswordResetToken(long id) {
        this.id = id;
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    /**
     * Full Constructor
     * @param token The user token must not be null
     * @param user The User for which the token should be created, it must not be null
     * @param creationDateTime The date time when this request was created
     * @param expirationMinutes The length, in minutes, for which whis token will be valid . If zero, it will be
     *                          assigned a default value of 120 minutes (2 hours)
     * @throws IllegalArgumentException If the token, user or creationDateTime are null
     */
    public PasswordResetToken(String token, User user, LocalDateTime creationDateTime, int expirationMinutes) {
        if (token == null || user == null || creationDateTime == null) {
            throw new IllegalArgumentException("The Token, User or LocalDateTime can't be null");
        }

        if (expirationMinutes == 0) {
            logger.warn("The token expiration length in minutes is zero. Assigning the default value {}", DEFAULT_TOKEN_MINUTES);
            expirationMinutes = DEFAULT_TOKEN_MINUTES;
        }
        this.token = token;
        this.user = user;
        expiryDate = creationDateTime.plusMinutes(expirationMinutes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetToken that = (PasswordResetToken) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
