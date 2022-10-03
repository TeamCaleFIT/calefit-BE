package com.calefit.member.domain;

import com.calefit.member.exception.NotFoundAlgorithmException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Embeddable
public class Password {

    private String salt;
    private String passwordValue;

    public Password(String requestPassword, String salt) {
        String[] saltAndPassword = hashing(requestPassword, salt);
        this.salt = saltAndPassword[0];
        this.passwordValue = saltAndPassword[1];
    }

    public boolean validatePassword(String requestPassword) {
        String[] saltAndPassword = hashing(requestPassword, salt);
        String requestPasswordValue = saltAndPassword[1];
        return Objects.equals(requestPasswordValue, passwordValue);
    }

    private String[] hashing(String getPassword, String salt) {
        String[] saltAndPassword = new String[2];
        byte[] requestPassword = getPassword.getBytes();

        if (Objects.isNull(salt)) {
            salt = generateSalt();
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new NotFoundAlgorithmException();
        }

        for (int i = 0; i < 10000; i++) {
            String temp = byteToString(requestPassword) + salt;
            md.update(temp.getBytes());
            requestPassword = md.digest();
        }

        saltAndPassword[0] = salt;
        saltAndPassword[1] = byteToString(requestPassword);

        return saltAndPassword;
    }

    private String generateSalt() {
        SecureRandom randomNumber = new SecureRandom();
        byte[] temp = new byte[16];
        randomNumber.nextBytes(temp);
        return byteToString(temp);
    }
    private String byteToString(byte[] temp) {
        StringBuilder sb = new StringBuilder();
        for (byte x : temp) {
            sb.append(String.format("%02x", x));
        }
        return sb.toString();
    }

}
