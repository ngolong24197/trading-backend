package com.DrakeN.trading.Response.Request;

import com.DrakeN.trading.Domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordRequest {


     private String sendTo;
     private String otp;
     private VerificationType type;

    public VerificationType getType() {
        return type;
    }

    public void setType(VerificationType type) {
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
