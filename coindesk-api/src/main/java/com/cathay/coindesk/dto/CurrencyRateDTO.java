package com.cathay.coindesk.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrencyRateDTO {

    private Integer id;
    private Integer currencyId;
    private LocalDateTime updateTime;
    private BigDecimal rate;

    public CurrencyRateDTO() {}

    public CurrencyRateDTO(Integer id, Integer currencyId, LocalDateTime updateTime, BigDecimal rate) {
        this.id = id;
        this.currencyId = currencyId;
        this.updateTime = updateTime;
        this.rate = rate;
    }

    public CurrencyRateDTO(Integer currencyId, LocalDateTime updateTime, BigDecimal rate) {
        this.currencyId = currencyId;
        this.updateTime = updateTime;
        this.rate = rate;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "CurrencyRateDTO{" +
                "id=" + id +
                ", currencyId=" + currencyId +
                ", updateTime=" + updateTime +
                ", rate=" + rate +
                '}';
    }
}