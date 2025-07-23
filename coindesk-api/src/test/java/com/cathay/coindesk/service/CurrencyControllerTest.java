package com.cathay.coindesk.service;

import com.cathay.coindesk.controller.CurrencyController;
import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.error.code.CoinDeskErrorCode;
import com.cathay.coindesk.utils.CoinDeskUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CurrencyService currencyService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void getAllCurrencies_ShouldReturnAllCurrencies() throws Exception {
        // Given
        CurrencyModel usd = new CurrencyModel(1, "USD", "美元");
        CurrencyModel eur = new CurrencyModel(2, "EUR", "歐元");
        when(currencyService.getAllCurrencies()).thenReturn(Arrays.asList(usd, eur));
        
        // When & Then
        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].chineseName").value("美元"))
                .andExpect(jsonPath("$[1].code").value("EUR"))
                .andExpect(jsonPath("$[1].chineseName").value("歐元"));
    }
    
    @Test
    void getCurrencyById_WhenExists_ShouldReturnCurrency() throws Exception {
        // Given
        CurrencyModel usd = new CurrencyModel(1, "USD", "美元");
        when(currencyService.getCurrencyById(1)).thenReturn(usd);
        
        // When & Then
        mockMvc.perform(get("/api/currencies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.chineseName").value("美元"));
    }
    
    @Test
    void getCurrencyById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        when(currencyService.getCurrencyById(999)).thenThrow(CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_NOT_FOUND, ""));
        
        // When & Then
        mockMvc.perform(get("/api/currencies/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createCurrency_WithValidData_ShouldCreateSuccessfully() throws Exception {
        // Given
        CurrencyModel inputDto = new CurrencyModel("GBP", "英鎊");
        CurrencyModel savedDto = new CurrencyModel(3, "GBP", "英鎊");
        when(currencyService.createCurrency(any(CurrencyModel.class))).thenReturn(savedDto);
        
        // When & Then
        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.code").value("GBP"))
                .andExpect(jsonPath("$.chineseName").value("英鎊"));
    }
    
    @Test
    void createCurrency_WithInvalidData_ShouldReturn400() throws Exception {
        // Given - empty code
        CurrencyModel invalidDto = new CurrencyModel("", "英鎊");
        
        // When & Then
        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void updateCurrency_WithValidData_ShouldUpdateSuccessfully() throws Exception {
        // Given
        CurrencyModel inputDto = new CurrencyModel("USD", "美金");
        CurrencyModel updatedDto = new CurrencyModel(1, "USD", "美金");
        when(currencyService.updateCurrency(eq(1), any(CurrencyModel.class))).thenReturn(updatedDto);
        
        // When & Then
        mockMvc.perform(put("/api/currencies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.chineseName").value("美金"));
    }
    
    @Test
    void deleteCurrency_WhenExists_ShouldDeleteSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/currencies/1"))
                .andExpect(status().isNoContent());
    }
}