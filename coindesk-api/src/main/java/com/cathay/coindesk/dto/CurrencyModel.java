package com.cathay.coindesk.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

/**
 * 幣別資料
 */
@Builder
public class CurrencyModel {

    private Integer id;

    @NotBlank(message = "Currency code cannot be blank")
    private String code;

    @NotBlank(message = "Chinese name cannot be blank")
    private String chineseName;

    public CurrencyModel() {
    }

    public CurrencyModel(Integer id, String code, String chineseName) {
        this.id = id;
        this.code = code;
        this.chineseName = chineseName;
    }

    public CurrencyModel(String code, String chineseName) {
        this.code = code;
        this.chineseName = chineseName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", chineseName='" + chineseName + '\'' +
                '}';
    }
}
