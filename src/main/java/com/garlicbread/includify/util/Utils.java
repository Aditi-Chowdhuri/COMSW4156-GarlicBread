package com.garlicbread.includify.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {

    public static String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
