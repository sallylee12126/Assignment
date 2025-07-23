package com.cathay.coindesk.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "currency")
public class CurrencyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", length = 10, unique = true, nullable = false)
    @NotBlank(message = "幣別代碼不能為空")
    @Size(max = 10, message = "幣別代碼長度不能超過10字元")
    private String code;

    @Column(name = "chinese_name", length = 100, nullable = false)
    @NotBlank(message = "中文名稱不能為空")
    @Size(max = 100, message = "中文名稱長度不能超過100字元")
    private String chineseName;

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
}