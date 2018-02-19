package com.f6rnando.web.domain.frontend;

/************************************
 Created by f6rnando@gmail.com
 2018-02-16
 *************************************/

public class ProAccountPayload extends BasicAccountPayload {

    /*      FIELDS      */

    private String cardNumber;

    private String cardCode;

    private String cardMonth;

    private String cardYear;

    /*      METHODS     */

    public ProAccountPayload() {
    }

    public ProAccountPayload(String email, String username, String password, String confirmPassword, String firstName, String lastName, String description, String phoneNumber, String country, String cardNumber, String cardCode, String cardMonth, String cardYear) {
        super(email, username, password, confirmPassword, firstName, lastName, description, phoneNumber, country);
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.cardMonth = cardMonth;
        this.cardYear = cardYear;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardMonth() {
        return cardMonth;
    }

    public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }

    public String getCardYear() {
        return cardYear;
    }

    public void setCardYear(String cardYear) {
        this.cardYear = cardYear;
    }


}
