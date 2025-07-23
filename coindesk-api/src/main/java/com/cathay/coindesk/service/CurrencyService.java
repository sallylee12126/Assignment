package com.cathay.coindesk.service;

import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.dto.CurrencyRateModel;
import com.cathay.coindesk.error.code.CoinDeskErrorCode;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.persistence.entity.CurrencyEntity;
import com.cathay.coindesk.persistence.entity.CurrencyRateEntity;
import com.cathay.coindesk.repository.CurrencyRateRepository;
import com.cathay.coindesk.repository.CurrencyRepository;
import com.cathay.coindesk.utils.CoinDeskUtils;
import com.cathay.coindesk.validatior.CommonArgsValidator;
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

    public List<CurrencyModel> getAllCurrencies() {
        List<CurrencyEntity> entities = currencyRepository.findAll();
        CommonArgsValidator.notBlank(entities);

        return entities.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public CurrencyModel getCurrencyById(Integer id) throws ActionException {
        CurrencyEntity entity = currencyRepository.findById(id)
                .orElseThrow(() -> CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_NOT_FOUND, id));
        return convertToModel(entity);
    }

    public CurrencyModel getCurrencyByCode(String code) throws ActionException {
        CurrencyEntity entity = currencyRepository.findByCode(code)
                .orElseThrow(() -> CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_NOT_FOUND, code));
        return convertToModel(entity);
    }

    public CurrencyModel createCurrency(CurrencyModel model) throws ActionException {
        if (currencyRepository.existsByCode(model.getCode())) {
            throw CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_ALREADY_EXISTS, model.getCode());
        }

        CurrencyEntity savedEntity = currencyRepository.save(convertToEntity(model));
        return convertToModel(savedEntity);
    }

    public CurrencyModel updateCurrency(Integer id, CurrencyModel model) throws ActionException {
        CurrencyEntity entity = currencyRepository.findById(id)
                .orElseThrow(() -> CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_NOT_FOUND, id));

        if (!entity.getCode().equals(model.getCode()) && currencyRepository.existsByCode(model.getCode())) {
            throw CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_ALREADY_EXISTS, model.getCode());
        }

        entity.setCode(model.getCode());
        entity.setChineseName(model.getChineseName());

        CurrencyEntity savedEntity = currencyRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public void deleteCurrency(Integer id) throws ActionException {
        if (!currencyRepository.existsById(id)) {
            throw CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_NOT_FOUND, id);
        }

        List<CurrencyRateEntity> rates = currencyRateRepository.findByCurrencyId(id);
        currencyRateRepository.deleteAll(rates);
        currencyRepository.deleteById(id);
    }

    // Currency Rate operations
    public void updateCurrencyRate(String currencyCode, BigDecimal rate) throws ActionException {
        CurrencyEntity currency = currencyRepository.findByCode(currencyCode)
                .orElseThrow(() -> CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_NOT_FOUND, currencyCode));

        CurrencyRateEntity rateEntity = CurrencyRateEntity.builder()
                .currencyId(currency.getId())
                .updateTime(LocalDateTime.now())
                .rate(rate)
                .build();
        currencyRateRepository.save(rateEntity);
    }

    public List<CurrencyRateModel> getAllCurrencyRates() {
        return currencyRateRepository.findAllOrderByUpdateTimeDesc().stream()
                .map(this::convertRateToModel)
                .collect(Collectors.toList());
    }

    public CurrencyRateModel getLatestCurrencyRate(Integer currencyId) throws ActionException {
        CurrencyRateEntity rateEntity = currencyRateRepository.findLatestByCurrencyId(currencyId)
                .orElseThrow(() -> CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_RATE_NOT_FOUND, currencyId));
        return convertRateToModel(rateEntity);
    }

    // Conversion methods
    private CurrencyModel convertToModel(CurrencyEntity entity) {
        return new CurrencyModel(entity.getId(), entity.getCode(), entity.getChineseName());
    }

    private CurrencyEntity convertToEntity(CurrencyModel model) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.setCode(model.getCode());
        entity.setChineseName(model.getChineseName());
        return entity;
    }

    private CurrencyRateModel convertRateToModel(CurrencyRateEntity entity) {
        return new CurrencyRateModel(entity.getId(), entity.getCurrencyId(),
                entity.getUpdateTime(), entity.getRate());
    }
}