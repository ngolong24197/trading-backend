package com.DrakeN.trading.Enitty;

import com.DrakeN.trading.Domain.WALLET_TRANSACTION_TYPE;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WALLET_TRANSACTION_TYPE type;


}
