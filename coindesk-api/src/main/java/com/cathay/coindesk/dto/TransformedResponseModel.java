package com.cathay.coindesk.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public class TransformedResponseModel {

    private String updateTime;
    private List<CurrencyRateInfo> currencies;

    public TransformedResponseModel() {}

    public TransformedResponseModel(String updateTime, List<CurrencyRateInfo> currencies) {
        this.updateTime = updateTime;
        this.currencies = currencies;
    }

    // Getters and Setters
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<CurrencyRateInfo> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyRateInfo> currencies) {
        this.currencies = currencies;
    }

    public static class CurrencyRateInfo {
        private String currencyCode;
        private String currencyChineseName;
        private BigDecimal rate;

        public CurrencyRateInfo() {}

        public CurrencyRateInfo(String currencyCode, String currencyChineseName, BigDecimal rate) {
            this.currencyCode = currencyCode;
            this.currencyChineseName = currencyChineseName;
            this.rate = rate;
        }

        // Getters and Setters
        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCurrencyChineseName() {
            return currencyChineseName;
        }

        public void setCurrencyChineseName(String currencyChineseName) {
            this.currencyChineseName = currencyChineseName;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        @Override
        public String toString() {
            return "CurrencyRateInfo{" +
                    "currencyCode='" + currencyCode + '\'' +
                    ", currencyChineseName='" + currencyChineseName + '\'' +
                    ", rate=" + rate +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TransformedResponseDTO{" +
                "updateTime='" + updateTime + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}