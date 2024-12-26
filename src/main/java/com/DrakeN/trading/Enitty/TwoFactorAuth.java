package com.DrakeN.trading.Enitty;

import com.DrakeN.trading.Domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnable = false;
    private VerificationType sendTo;

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
