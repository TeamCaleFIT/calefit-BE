package com.calefit.member.member.util;

import com.calefit.member.member.exception.NotFoundAlgorithmException;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

@Component
public class PasswordEncoder {
    private final int SALT_SIZE = 16;

    public String[] hashing(String getPassword, String salt) {
        String[] saltAndPassword = new String[2];
        byte[] requestPassword = getPassword.getBytes();

        if (Objects.isNull(salt)) {
            salt = getSalt();
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

    private String getSalt() {
        SecureRandom randomNumber = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
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
