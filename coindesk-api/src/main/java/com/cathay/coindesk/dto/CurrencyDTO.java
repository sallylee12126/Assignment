package com.cathay.coindesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 幣別資料
 */
@Builder
public class CurrencyDTO {

    private Integer id;

    @NotBlank(message = "Currency code cannot be blank")
    private String code;

    @NotBlank(message = "Chinese name cannot be blank")
    private String chineseName;

    public CurrencyDTO() {
    }

    public CurrencyDTO(Integer id, String code, String chineseName) {
        this.id = id;
        this.code = code;
        this.chineseName = chineseName;
    }

    public CurrencyDTO(String code, String chineseName) {
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
