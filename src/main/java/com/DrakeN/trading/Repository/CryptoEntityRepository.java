package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoEntityRepository extends JpaRepository<CryptoEntity,String> {
}
