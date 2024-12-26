package com.DrakeN.trading.Ulti;

import java.util.Random;

public class otpUltis {

    public static String generateOtp(){
        int otpLength = 6;
        Random rand = new Random();
        StringBuilder otp = new StringBuilder(otpLength);
        for(int i = 0; i < otpLength; i++){
            otp.append(rand.nextInt(10));
        }
        return otp.toString();
    }
}
