package com.cathay.coindesk.service;

import com.cathay.coindesk.dto.CurrencyDTO;
import com.cathay.coindesk.dto.CurrencyRateDTO;
import com.cathay.coindesk.persistence.entity.CurrencyEntity;
import com.cathay.coindesk.persistence.entity.CurrencyRateEntity;
import com.cathay.coindesk.repository.CurrencyRateRepository;
import com.cathay.coindesk.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    // Currency CRUD operations
    public List<CurrencyDTO> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyDTO> getCurrencyById(Long id) {
        return currencyRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<CurrencyDTO> getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code)
                .map(this::convertToDTO);
    }

    public CurrencyDTO createCurrency(CurrencyDTO currencyDTO) {
        if (currencyRepository.existsByCode(currencyDTO.getCode())) {
            throw new RuntimeException("Currency with code " + currencyDTO.getCode() + " already exists");
        }

        CurrencyEntity entity = convertToEntity(currencyDTO);
        CurrencyEntity savedEntity = currencyRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    public CurrencyDTO updateCurrency(Long id, CurrencyDTO currencyDTO) {
        CurrencyEntity entity = currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));

        // Check if code already exists for other currencies
        if (!entity.getCode().equals(currencyDTO.getCode()) &&
                currencyRepository.existsByCode(currencyDTO.getCode())) {
            throw new RuntimeException("Currency with code " + currencyDTO.getCode() + " already exists");
        }

        entity.setCode(currencyDTO.getCode());
        entity.setChineseName(currencyDTO.getChineseName());

        CurrencyEntity savedEntity = currencyRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    public void deleteCurrency(Long id) {
        if (!currencyRepository.existsById(id)) {
            throw new RuntimeException("Currency not found with id: " + id);
        }

        // Delete related currency rates first
        List<CurrencyRateEntity> rates = currencyRateRepository.findByCurrencyId(id);
        currencyRateRepository.deleteAll(rates);

        currencyRepository.deleteById(id);
    }

    // Currency Rate operations
    public void updateCurrencyRate(String currencyCode, BigDecimal rate) {
        Optional<CurrencyEntity> currencyOpt = currencyRepository.findByCode(currencyCode);
        if (currencyOpt.isPresent()) {
            CurrencyEntity currency = currencyOpt.get();
            CurrencyRateEntity rateEntity = CurrencyRateEntity.builder()
                    .currencyId(currency.getId()).updateTime(LocalDateTime.now()).rate(rate).build();
            currencyRateRepository.save(rateEntity);
        }
    }

    public List<CurrencyRateDTO> getAllCurrencyRates() {
        return currencyRateRepository.findAllOrderByUpdateTimeDesc().stream()
                .map(this::convertRateToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyRateDTO> getLatestCurrencyRate(Long currencyId) {
        return currencyRateRepository.findLatestByCurrencyId(currencyId)
                .map(this::convertRateToDTO);
    }

    // Conversion methods
    private CurrencyDTO convertToDTO(CurrencyEntity entity) {
        return new CurrencyDTO(entity.getId(), entity.getCode(), entity.getChineseName());
    }

    private CurrencyEntity convertToEntity(CurrencyDTO dto) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.setCode(dto.getCode());
        entity.setChineseName(dto.getChineseName());
        return entity;
    }

    private CurrencyRateDTO convertRateToDTO(CurrencyRateEntity entity) {
        return new CurrencyRateDTO(entity.getId(), entity.getCurrencyId(),
                entity.getUpdateTime(), entity.getRate());
    }
}