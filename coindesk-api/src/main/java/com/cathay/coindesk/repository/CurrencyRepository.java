package com.cathay.coindesk.repository;

import com.cathay.coindesk.persistence.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Integer> {

    Optional<CurrencyEntity> findByCode(String code);

    boolean existsByCode(String code);
}