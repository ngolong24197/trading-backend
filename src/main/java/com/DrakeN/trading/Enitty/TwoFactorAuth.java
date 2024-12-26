package com.DrakeN.trading.Enitty;

import com.DrakeN.trading.Domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnable = false;
    private VerificationType sendTo;

}
