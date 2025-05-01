package com.integracioncomunitaria.notificationapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Hash {
    public static void main(String[] args) {
        String hash = new BCryptPasswordEncoder().encode("1234");
        System.out.println("El hash es "+hash);



    }
}
