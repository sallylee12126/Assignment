package com.cathay.coindesk.api.data.impl;

import com.cathay.coindesk.api.data.ApiRsBody;
import com.cathay.coindesk.dto.CoindeskResponseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinDeskApiRs extends ApiRsBody {

	@JsonProperty("data")
	private CoindeskResponseModel data;
	
}