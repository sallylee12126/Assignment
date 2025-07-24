package com.cathay.coindesk.service;

import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.exception.ActionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private WebApplicationContext context;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeEach
    void setUp() throws ActionException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        currencyService.createCurrency(new CurrencyModel("USD", "美元"));
        currencyService.createCurrency(new CurrencyModel("GBP", "英鎊"));
        currencyService.createCurrency(new CurrencyModel("EUR", "歐元"));
    }

    @Test
    void shouldPerformFullCurrencyCrudFlow() throws Exception {
        // Create
        CurrencyModel newCurrency = new CurrencyModel("BTC", "比特幣");

        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCurrency)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("BTC"))
                .andExpect(jsonPath("$.data.chineseName").value("比特幣"));

        // Read all
        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(4))); // Assuming 3 initial + 1 new

        // Read by code
        mockMvc.perform(get("/api/currencies/code/BTC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("BTC"))
                .andExpect(jsonPath("$.data.chineseName").value("比特幣"));

        // Update
        Integer id = currencyService.getCurrencyByCode("BTC").getId();
        CurrencyModel updated = new CurrencyModel("BTC", "Bitcoin");

        mockMvc.perform(put("/api/currencies/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.chineseName").value("Bitcoin"));

        // Delete
        mockMvc.perform(delete("/api/currencies/" + id))
                .andExpect(status().isNoContent());

        // Verify deleted
        mockMvc.perform(get("/api/currencies/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOriginalCoindeskData() throws Exception {
        mockMvc.perform(get("/api/coindesk/original"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.time").exists())
                .andExpect(jsonPath("$.data.disclaimer").exists())
                .andExpect(jsonPath("$.data.bpi").exists());
    }

    @Test
    void shouldTransformCoindeskDataCorrectly() throws Exception {
        mockMvc.perform(get("/api/coindesk/transformed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.updateTime", matchesPattern("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}")))
                .andExpect(jsonPath("$.data.currencies", is(not(empty()))))
                .andExpect(jsonPath("$.data.currencies[0].currencyCode").exists())
                .andExpect(jsonPath("$.data.currencies[0].currencyChineseName").exists())
                .andExpect(jsonPath("$.data.currencies[0].rate").exists());
    }

    @Test
    void shouldFailValidation_WhenMissingFields() throws Exception {
        // Empty code
        CurrencyModel invalid1 = new CurrencyModel("", "Invalid");

        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("VF001"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("code")));

        // Empty chineseName
        CurrencyModel invalid2 = new CurrencyModel("INV", "");

        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("VF001"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("chineseName")));
    }

    @Test
    void shouldFailOnDuplicateCurrencyCode() throws Exception {
        CurrencyModel duplicate = new CurrencyModel("USD", "另一個美元");

        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }
}