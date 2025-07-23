package com.cathay.coindesk.controller;

import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.rest.RestResult;
import com.cathay.coindesk.rest.RestStatus;
import com.cathay.coindesk.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    /**
     * 查詢所有幣別
     */
    @GetMapping
    public RestResult<List<CurrencyModel>> getAllCurrencies() {
        List<CurrencyModel> currencies = currencyService.getAllCurrencies();
        return new RestResult<>(RestStatus.SUCCESS, currencies);
    }

    /**
     * 根據 ID 查詢幣別
     */
    @GetMapping("/{id}")
    public RestResult<CurrencyModel> getCurrencyById(@PathVariable Integer id) throws ActionException {
        CurrencyModel currency = currencyService.getCurrencyById(id);
        return new RestResult<>(RestStatus.SUCCESS, currency);
    }

    /**
     * 根據 Code 查詢幣別
     */
    @GetMapping("/code/{code}")
    public RestResult<CurrencyModel> getCurrencyByCode(@PathVariable String code) throws ActionException {
        CurrencyModel currency = currencyService.getCurrencyByCode(code);
        return new RestResult<>(RestStatus.SUCCESS, currency);
    }

    /**
     * 新增幣別
     */
    @PostMapping
    public RestResult<CurrencyModel> createCurrency(@Valid @RequestBody CurrencyModel currencyModel) throws ActionException {
        CurrencyModel createdCurrency = currencyService.createCurrency(currencyModel);
        return new RestResult<>(RestStatus.SUCCESS, createdCurrency);
    }

    /**
     * 更新幣別
     */
    @PutMapping("/{id}")
    public RestResult<CurrencyModel> updateCurrency(@PathVariable Integer id,
                                                    @Valid @RequestBody CurrencyModel currencyModel) throws ActionException {
        CurrencyModel updatedCurrency = currencyService.updateCurrency(id, currencyModel);
        return new RestResult<>(RestStatus.SUCCESS, updatedCurrency);
    }

    /**
     * 刪除幣別
     */
    @DeleteMapping("/{id}")
    public RestResult<Void> deleteCurrency(@PathVariable Integer id) throws ActionException {
        currencyService.deleteCurrency(id);
        return new RestResult<>(RestStatus.SUCCESS, null);
    }
}