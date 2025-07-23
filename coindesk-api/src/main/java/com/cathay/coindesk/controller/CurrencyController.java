package com.cathay.coindesk.controller;

import com.cathay.coindesk.dto.CurrencyDTO;
import com.cathay.coindesk.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies() {
        List<CurrencyDTO> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDTO> getCurrencyById(@PathVariable Long id) {
        Optional<CurrencyDTO> currency = currencyService.getCurrencyById(id);
        return currency.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CurrencyDTO> getCurrencyByCode(@PathVariable String code) {
        Optional<CurrencyDTO> currency = currencyService.getCurrencyByCode(code);
        return currency.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) {
        try {
            CurrencyDTO createdCurrency = currencyService.createCurrency(currencyDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDTO> updateCurrency(@PathVariable Long id,
                                                      @Valid @RequestBody CurrencyDTO currencyDTO) {
        try {
            CurrencyDTO updatedCurrency = currencyService.updateCurrency(id, currencyDTO);
            return ResponseEntity.ok(updatedCurrency);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        try {
            currencyService.deleteCurrency(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}