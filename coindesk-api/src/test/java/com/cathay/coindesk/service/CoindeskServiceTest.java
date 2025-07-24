package com.cathay.coindesk.service;

import com.cathay.coindesk.api.data.impl.CoinDeskApiRs;
import com.cathay.coindesk.dto.CoindeskResponseModel;
import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.dto.TransformedResponseModel;
import com.cathay.coindesk.error.code.CoinDeskErrorCode;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.utils.CoinDeskUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoindeskServiceTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CoindeskService coindeskService;

    private CoindeskResponseModel mockCoindeskResponse;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(coindeskService, "coindeskApiUrl", "https://kengp3.github.io/blog/coindesk.json");

        mockCoindeskResponse = new CoindeskResponseModel();

        CoindeskResponseModel.TimeInfo timeInfo = new CoindeskResponseModel.TimeInfo();
        timeInfo.setUpdatedISO("2024-01-01T10:00:00Z");
        mockCoindeskResponse.setTime(timeInfo);

        Map<String, CoindeskResponseModel.CurrencyInfo> bpi = new HashMap<>();

        CoindeskResponseModel.CurrencyInfo usdInfo = new CoindeskResponseModel.CurrencyInfo();
        usdInfo.setCode("USD");
        usdInfo.setRateFloat(new BigDecimal(50000.0));
        bpi.put("USD", usdInfo);

        CoindeskResponseModel.CurrencyInfo eurInfo = new CoindeskResponseModel.CurrencyInfo();
        eurInfo.setCode("EUR");
        eurInfo.setRateFloat(new BigDecimal(45000.0));
        bpi.put("EUR", eurInfo);

        mockCoindeskResponse.setBpi(bpi);
    }

    @Test
    void formatUpdateTime_ShouldFormatCorrectly() throws ActionException {
        when(currencyService.getCurrencyByCode("USD")).thenReturn(new CurrencyModel("USD", "美元"));
        when(currencyService.getCurrencyByCode("EUR")).thenReturn(new CurrencyModel("EUR", "歐元"));

        CoinDeskApiRs apiRs = new CoinDeskApiRs();
        apiRs.setData(mockCoindeskResponse);

        CoindeskService spyService = spy(coindeskService);
        doReturn(apiRs).when(spyService).getCoindeskData();

        TransformedResponseModel result = spyService.getTransformedCoindeskData();

        assertNotNull(result);
        assertNotNull(result.getUpdateTime());
        assertTrue(result.getUpdateTime().matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void getTransformedCoindeskData_ShouldTransformDataCorrectly() throws ActionException {
        when(currencyService.getCurrencyByCode("USD")).thenReturn(new CurrencyModel("USD", "美元"));
        when(currencyService.getCurrencyByCode("EUR")).thenReturn(new CurrencyModel("EUR", "歐元"));

        CoinDeskApiRs apiRs = new CoinDeskApiRs();
        apiRs.setData(mockCoindeskResponse);

        CoindeskService spyService = spy(coindeskService);
        doReturn(apiRs).when(spyService).getCoindeskData();

        TransformedResponseModel result = spyService.getTransformedCoindeskData();

        assertNotNull(result);
        assertEquals(2, result.getCurrencies().size());

        TransformedResponseModel.CurrencyRateInfo usdInfo = result.getCurrencies().stream()
                .filter(c -> "USD".equals(c.getCurrencyCode()))
                .findFirst().orElse(null);

        assertNotNull(usdInfo);
        assertEquals("USD", usdInfo.getCurrencyCode());
        assertEquals("美元", usdInfo.getCurrencyChineseName());
        assertEquals(0, usdInfo.getRate().compareTo(BigDecimal.valueOf(50000)));

        verify(currencyService).updateOrCreateCurrencyRate("USD", new BigDecimal(50000.0));
        verify(currencyService).updateOrCreateCurrencyRate("EUR", new BigDecimal(45000.0));
    }
}