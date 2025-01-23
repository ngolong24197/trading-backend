package com.DrakeN.trading.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DrakeN.trading.Enitty.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);


} 


