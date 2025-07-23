package com.cathay.coindesk.service;

import com.cathay.coindesk.api.data.ApiRsBody;
import com.cathay.coindesk.api.data.impl.CoinDeskApiRs;
import com.cathay.coindesk.dto.CoindeskResponseModel;
import com.cathay.coindesk.dto.CurrencyModel;
import com.cathay.coindesk.dto.TransformedResponseModel;
import com.cathay.coindesk.error.code.CoinDeskErrorCode;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.utils.CoinDeskUtils;
import com.cathay.coindesk.validatior.CommonArgsValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CoindeskService {

    @Value("${coindesk.api.url}")
    private String coindeskApiUrl;

    @Autowired
    private CurrencyService currencyService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CoinDeskApiRs getCoindeskData() throws ActionException {
        try {
            String response = restTemplate.getForObject(coindeskApiUrl, String.class);
            CoindeskResponseModel model = objectMapper.readValue(response, CoindeskResponseModel.class);

            CoinDeskApiRs rs = new CoinDeskApiRs();
            rs.setStatusCode(ApiRsBody.SUCCESS);
            rs.setData(model);
            return rs;

        } catch (JsonProcessingException e) {
            log.error("JSON 解析失敗：Coindesk API 回傳格式異常", e);
            throw CoinDeskUtils.newActionException(CoinDeskErrorCode.COINDESK_API_JSON_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("呼叫 Coindesk API 發生例外", e);
            throw CoinDeskUtils.newActionException(CoinDeskErrorCode.COINDESK_API_ERROR, e.getMessage());
        }
    }

    public TransformedResponseModel getTransformedCoindeskData() throws ActionException {
        CoindeskResponseModel coindeskData = getCoindeskData().getData();
        CommonArgsValidator.notNull(coindeskData);

        // Format update time
        String formattedTime = formatUpdateTime(coindeskData.getTime().getUpdatedISO());

        // Transform currency data
        List<TransformedResponseModel.CurrencyRateInfo> currencyInfoList = new ArrayList<>();

        if (coindeskData.getBpi() != null) {
            for (Map.Entry<String, CoindeskResponseModel.CurrencyInfo> entry : coindeskData.getBpi().entrySet()) {
                String currencyCode = entry.getValue().getCode();
                BigDecimal rate = entry.getValue().getRateFloat();

                // Get Chinese name from database or use default
                String chineseName = getCurrencyChineseName(currencyCode);

                // Update currency rate in database
                currencyService.updateCurrencyRate(currencyCode, rate);

                currencyInfoList.add(new TransformedResponseModel.CurrencyRateInfo(
                        currencyCode, chineseName, rate));
            }
        }

        return TransformedResponseModel.builder()
                .updateTime(formattedTime)
                .currencies(currencyInfoList)
                .build();
    }

    private String formatUpdateTime(String isoDateTime) throws ActionException {
        try {
            OffsetDateTime odt = OffsetDateTime.parse(isoDateTime); // 自動解析 ISO 格式 + 時區
            LocalDateTime localDateTime = odt.toLocalDateTime(); // 轉成沒有時區的時間
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            return localDateTime.format(formatter);
        } catch (DateTimeParseException e) {
            log.error("Failed to format update time: {}", isoDateTime, e);
            throw CoinDeskUtils.newActionException(CoinDeskErrorCode.COINDESK_TIME_PARSE_ERROR, e.getMessage());
        }
    }

    private String getCurrencyChineseName(String currencyCode) throws ActionException {
        CurrencyModel model = currencyService.getCurrencyByCode(currencyCode);
        String name = model.getChineseName();

        if (StringUtils.isBlank(name)) {
            throw CoinDeskUtils.newActionException(CoinDeskErrorCode.CURRENCY_CH_ERROR, currencyCode);
        }

        return name;
    }

}