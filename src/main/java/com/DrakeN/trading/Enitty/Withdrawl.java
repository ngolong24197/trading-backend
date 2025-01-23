package com.DrakeN.trading.Enitty;


import com.DrakeN.trading.Domain.WITHDRAWL_STATUS;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Withdrawl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WITHDRAWL_STATUS status;

    private Long amount;

    @ManyToOne
    private User user;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public WITHDRAWL_STATUS getStatus() {
        return status;
    }

    public void setStatus(WITHDRAWL_STATUS status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private LocalDateTime date=LocalDateTime.now();
}
