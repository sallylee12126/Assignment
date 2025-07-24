package com.cathay.coindesk.controller;

import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.rest.RestResult;
import com.cathay.coindesk.rest.RestStatus;
import com.cathay.coindesk.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    /**
     * 查詢所有幣別
     */
    @GetMapping("/currencies")
    public RestResult<List<CurrencyModel>> getAllCurrencies() throws ActionException {
        List<CurrencyModel> currencies = currencyService.getAllCurrencies();
        return new RestResult<>(RestStatus.SUCCESS, currencies);
    }


    /**
     * 查詢所有幣別(含匯率資料)
     */
    @GetMapping("/currenciesWithRates")
    public RestResult<List<CurrencyModel>> getAllcurrenciesWithRates() throws ActionException {
        List<CurrencyModel> currencies = currencyService.getAllCurrenciesWithRate();
        return new RestResult<>(RestStatus.SUCCESS, currencies);
    }


    /**
     * 根據 ID 查詢幣別
     */
    @GetMapping("/id/{id}")
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
    @PostMapping("/create")
    public RestResult<CurrencyModel> createCurrency(@Valid @RequestBody CurrencyModel currencyModel) throws ActionException {
        CurrencyModel createdCurrency = currencyService.createCurrency(currencyModel);
        return new RestResult<>(RestStatus.SUCCESS, createdCurrency);
    }

    /**
     * 根據 code 更新幣別
     */
    @PutMapping("/code/{code}")
    public RestResult<CurrencyModel> updateCurrencyByCode(@PathVariable String code,
                                                          @Valid @RequestBody CurrencyModel model) throws ActionException {
        CurrencyModel updated = currencyService.updateCurrencyByCode(code, model);
        return new RestResult<>(RestStatus.SUCCESS, updated);
    }

    /**
     * 根據 code 刪除幣別
     */
    @DeleteMapping("/code/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RestResult<Void> deleteCurrencyByCode(@PathVariable String code) throws ActionException {
        currencyService.deleteCurrencyByCode(code);
        return new RestResult<>(RestStatus.SUCCESS, null);
    }

    /**
     * 根據 id 更新幣別
     */
    @PutMapping("/id/{id}")
    public RestResult<CurrencyModel> updateCurrencyById(@PathVariable Integer id,
                                                        @Valid @RequestBody CurrencyModel model) throws ActionException {
        CurrencyModel updated = currencyService.updateCurrency(id, model);
        return new RestResult<>(RestStatus.SUCCESS, updated);
    }

    /**
     * 根據 id 刪除幣別
     */
    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RestResult<Void> deleteCurrencyById(@PathVariable Integer id) throws ActionException {
        currencyService.deleteCurrency(id);
        return new RestResult<>(RestStatus.SUCCESS, null);
    }
}