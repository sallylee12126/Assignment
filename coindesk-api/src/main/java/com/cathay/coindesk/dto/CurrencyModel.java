package com.cathay.coindesk.dto;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class
CurrencyModel {

    @NotBlank(message = "Currency code cannot be blank")
    private String code;

    @NotBlank(message = "Chinese name cannot be blank")
    private String chineseName;

    private BigDecimal rate; // 新增欄位

    public CurrencyModel() {}

    public CurrencyModel(String code, String chineseName) {
        this.code = code;
        this.chineseName = chineseName;
    }

    public CurrencyModel(String code, String chineseName, BigDecimal rate) {
        this.code = code;
        this.chineseName = chineseName;
        this.rate = rate;
    }

    // Getter/Setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getChineseName() { return chineseName; }
    public void setChineseName(String chineseName) { this.chineseName = chineseName; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "code='" + code + '\'' +
                ", chineseName='" + chineseName + '\'' +
                ", rate=" + rate +
                '}';
    }
}