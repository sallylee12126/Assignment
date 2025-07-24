package com.cathay.coindesk.service;

import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.persistence.entity.CurrencyEntity;
import com.cathay.coindesk.repository.CurrencyRateRepository;
import com.cathay.coindesk.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {
    
    @Mock
    private CurrencyRepository currencyRepository;
    
    @Mock
    private CurrencyRateRepository currencyRateRepository;
    
    @InjectMocks
    private CurrencyService currencyService;
    
    private CurrencyEntity usdEntity;
    private CurrencyEntity eurEntity;
    
    @BeforeEach
    void setUp() {
        usdEntity = new CurrencyEntity(1,"USD", "美元");
        usdEntity.setId(1);
        
        eurEntity = new CurrencyEntity(2,"EUR", "歐元");
        eurEntity.setId(2);
    }
    
    @Test
    void getAllCurrencies_ShouldReturnAllCurrencies() throws ActionException {
        // Given
        List<CurrencyEntity> entities = Arrays.asList(usdEntity, eurEntity);
        when(currencyRepository.findAll()).thenReturn(entities);
        
        // When
        List<CurrencyModel> result = currencyService.getAllCurrencies();
        
        // Then
        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getCode());
        assertEquals("美元", result.get(0).getChineseName());
        assertEquals("EUR", result.get(1).getCode());
        assertEquals("歐元", result.get(1).getChineseName());
    }
    
    @Test
    void getCurrencyById_WhenExists_ShouldReturnCurrency() throws ActionException {
        // Given
        when(currencyRepository.findById(1)).thenReturn(Optional.of(usdEntity));
        
        // When
        Optional<CurrencyModel> result = Optional.ofNullable(currencyService.getCurrencyById(1));
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("USD", result.get().getCode());
        assertEquals("美元", result.get().getChineseName());
    }
    
    @Test
    void getCurrencyById_WhenNotExists_ShouldThrowActionException() {
        // Given
        when(currencyRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        ActionException exception = assertThrows(ActionException.class, () -> {
            currencyService.getCurrencyById(999);
        });

        assertEquals("查無幣別資料", exception.getMessage()); // 或依照實際訊息內容調整
        assertEquals("D001", exception.getErrorCode()); // 可選：驗證錯誤碼
    }
    
    @Test
    void createCurrency_WhenCodeNotExists_ShouldCreateSuccessfully() throws ActionException {
        // Given
        CurrencyModel dto = new CurrencyModel("GBP", "英鎊");
        CurrencyEntity savedEntity = new CurrencyEntity(3,"GBP", "英鎊");

        when(currencyRepository.existsByCode("GBP")).thenReturn(false);
        when(currencyRepository.save(any(CurrencyEntity.class))).thenReturn(savedEntity);
        
        // When
        CurrencyModel result = currencyService.createCurrency(dto);
        
        // Then
        assertEquals("GBP", result.getCode());
        assertEquals("英鎊", result.getChineseName());
        verify(currencyRepository).save(any(CurrencyEntity.class));
    }


    @Test
    void createCurrency_WhenCodeExists_ShouldThrowActionException() {
        // Given
        CurrencyModel dto = new CurrencyModel("USD", "美元");
        when(currencyRepository.existsByCode("USD")).thenReturn(true);

        // When & Then
        ActionException exception = assertThrows(ActionException.class,
                () -> currencyService.createCurrency(dto));

        assertEquals("該幣別已存在", exception.getMessage());  // 根據 CoinDeskErrorCode 錯誤定義來調整
        assertEquals("D003", exception.getErrorCode());
        assertEquals("COINDESK", exception.getSystemId());
    }
    
    @Test
    void updateCurrency_WhenExists_ShouldUpdateSuccessfully() throws ActionException {
        // Given
        CurrencyModel dto = new CurrencyModel("EUR", "歐元");
        when(currencyRepository.findById(1)).thenReturn(Optional.of(usdEntity));
        when(currencyRepository.existsByCode("EUR")).thenReturn(false);
        when(currencyRepository.save(any(CurrencyEntity.class))).thenReturn(usdEntity);
        
        // When
        CurrencyModel result = currencyService.updateCurrency(1, dto);
        
        // Then
        assertEquals("EUR", result.getCode());
        verify(currencyRepository).save(usdEntity);
    }
    
    @Test
    void deleteCurrency_WhenExists_ShouldDeleteSuccessfully() throws ActionException {
        // Given
        when(currencyRepository.existsById(1)).thenReturn(true);
        when(currencyRateRepository.findByCurrencyId(1)).thenReturn(Arrays.asList());
        
        // When
        currencyService.deleteCurrency(1);
        
        // Then
        verify(currencyRepository).deleteById(1);
    }
    
    @Test
    void deleteCurrency_WhenNotExists_ShouldThrowException() {
        // Given
        when(currencyRepository.existsById(999)).thenReturn(false);
        
        // When & Then
        ActionException exception = assertThrows(ActionException.class, () -> currencyService.deleteCurrency(999));
        assertEquals("查無幣別資料", exception.getMessage());
    }
}