package com.cathay.coindesk.service;

import com.cathay.coindesk.dto.CurrencyModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CoindeskApiIntegrationTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private CurrencyService currencyService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @PostConstruct
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    void testCurrencyCRUDOperations() throws Exception {
        // Test CREATE
        CurrencyModel newCurrency = new CurrencyModel("BTC", "比特幣");
        
        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCurrency)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BTC"))
                .andExpect(jsonPath("$.chineseName").value("比特幣"));
        
        // Test READ ALL
        mockMvc.perform(get("/api/currencies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(7)); // 6 initial + 1 new
        
        // Test READ BY CODE
        mockMvc.perform(get("/api/currencies/code/BTC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BTC"))
                .andExpect(jsonPath("$.chineseName").value("比特幣"));
        
        // Get the created currency ID for update and delete
        Integer currencyId = currencyService.getCurrencyByCode("BTC").getId();
        
        // Test UPDATE
        CurrencyModel updatedCurrency = new CurrencyModel("BTC", "Bitcoin");
        
        mockMvc.perform(put("/api/currencies/" + currencyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCurrency)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BTC"))
                .andExpect(jsonPath("$.chineseName").value("Bitcoin"));
        
        // Test DELETE
        mockMvc.perform(delete("/api/currencies/" + currencyId))
                .andDo(print())
                .andExpect(status().isNoContent());
        
        // Verify deletion
        mockMvc.perform(get("/api/currencies/" + currencyId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCoindeskOriginalAPI() throws Exception {
        mockMvc.perform(get("/api/coindesk/original"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").exists())
                .andExpect(jsonPath("$.disclaimer").exists())
                .andExpect(jsonPath("$.bpi").exists());
    }
    
    @Test
    void testCoindeskTransformedAPI() throws Exception {
        mockMvc.perform(get("/api/coindesk/transformed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updateTime").exists())
                .andExpect(jsonPath("$.updateTime").value(org.hamcrest.Matchers.matchesPattern("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}")))
                .andExpect(jsonPath("$.currencies").isArray())
                .andExpect(jsonPath("$.currencies[0].currencyCode").exists())
                .andExpect(jsonPath("$.currencies[0].currencyChineseName").exists())
                .andExpect(jsonPath("$.currencies[0].rate").exists());
    }
    
    @Test
    void testCurrencyValidation() throws Exception {
        // Test with empty code
        CurrencyModel invalidCurrency = new CurrencyModel("", "Invalid");
        
        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCurrency)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        
        // Test with empty Chinese name
        invalidCurrency = new CurrencyModel("INV", "");
        
        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCurrency)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testDuplicateCurrencyCode() throws Exception {
        // Try to create currency with existing code
        CurrencyModel duplicateCurrency = new CurrencyModel("USD", "Another USD");
        
        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateCurrency)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}