package com.DrakeN.trading.Configuration;

import io.github.cdimascio.dotenv.Dotenv;


import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./trading-backend") // Ensure correct path
                .ignoreIfMissing() // Avoid crashing if not found
                .load();

        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
        System.setProperty("MAIL_USER", dotenv.get("MAIL_USER"));
        System.setProperty("MAIL_PASS", dotenv.get("MAIL_PASS"));
        System.setProperty("VNPAY_TMN_CODE", dotenv.get("VNPAY_TMN_CODE"));
        System.setProperty("VNPAY_SECRET", dotenv.get("VNPAY_SECRET"));
        System.setProperty("STRIPE_SECRET", dotenv.get("STRIPE_SECRET"));
    }
}
