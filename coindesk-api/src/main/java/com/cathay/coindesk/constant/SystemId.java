package com.cathay.coindesk.constant;

import lombok.AllArgsConstructor;

/**
 * <p>系統代碼表</p>
 */
@AllArgsConstructor
public enum SystemId {



	/** COINDESK */
	COINDESK("COINDESK", "001")
    ;

	/** 系統代碼 */
	public final String SYSTEM_ID;

	/** 系統識別碼 */
	public final String SYSTEM_CODE;
}



