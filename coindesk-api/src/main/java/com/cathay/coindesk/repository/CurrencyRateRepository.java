package com.cathay.coindesk.repository;

import com.cathay.coindesk.persistence.entity.CurrencyRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRateEntity, Integer> {

    List<CurrencyRateEntity> findByCurrencyId(Integer currencyId);

    Optional<CurrencyRateEntity> findTopByCurrencyIdOrderByUpdateTimeDesc(Integer currencyId);

    @Query("SELECT cr FROM CurrencyRateEntity cr ORDER BY cr.updateTime DESC")
    List<CurrencyRateEntity> findAllOrderByUpdateTimeDesc();
}