package com.cathay.coindesk.controller;

import com.cathay.coindesk.dto.CoindeskResponseModel;
import com.cathay.coindesk.dto.TransformedResponseModel;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.rest.RestResult;
import com.cathay.coindesk.rest.RestStatus;
import com.cathay.coindesk.service.CoindeskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/coindesk")
public class CoindeskController {

    @Autowired
    private CoindeskService coindeskService;

    @GetMapping("/original")
    public RestResult<CoindeskResponseModel> getOriginalCoindeskData() throws ActionException {
            CoindeskResponseModel data = coindeskService.getCoindeskData().getData();
            return new RestResult<>(RestStatus.SUCCESS, data);
    }

    @GetMapping("/transformed")
    public RestResult<TransformedResponseModel> getTransformedCoindeskData() throws Exception {
        TransformedResponseModel data = coindeskService.getTransformedCoindeskData();
        return new RestResult<>(RestStatus.SUCCESS, data);
    }
}