package com.DrakeN.trading.Response.Request;

import com.DrakeN.trading.Domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordRequest {


     private String sendTo;
     private String otp;
     private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
