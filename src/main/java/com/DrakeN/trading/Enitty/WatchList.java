package com.DrakeN.trading.Enitty;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class WatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @ManyToMany
    private List<CryptoEntity> coinsList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CryptoEntity> getCoinsList() {
        return coinsList;
    }

    public void setCoinsList(List<CryptoEntity> coinsList) {
        this.coinsList = coinsList;
    }
}
