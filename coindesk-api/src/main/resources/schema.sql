-- 幣別主檔表（加入自增主鍵）
CREATE TABLE IF NOT EXISTS currency
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '流水號',
    code         VARCHAR(10) UNIQUE NOT NULL COMMENT '幣別代碼',
    chinese_name VARCHAR(100)       NOT NULL COMMENT '幣別中文名稱'
);

-- 建立索引以提升查詢效能
CREATE UNIQUE INDEX IF NOT EXISTS idx_currency_code ON currency (code);
CREATE INDEX IF NOT EXISTS idx_currency_chinese_name ON currency (chinese_name);


CREATE TABLE IF NOT EXISTS currency_rate
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '流水號',
    currency_id BIGINT         NOT NULL COMMENT '對應 currencyEntity.id',
    update_time TIMESTAMP      NOT NULL COMMENT '匯率更新時間',
    rate        DECIMAL(20, 6) NOT NULL COMMENT '匯率',

    CONSTRAINT fk_currency_id FOREIGN KEY (currency_id) REFERENCES currency (id)
);

CREATE INDEX IF NOT EXISTS idx_rate_currency_id ON currency_rate (currency_id);
CREATE INDEX IF NOT EXISTS idx_rate_update_time ON currency_rate (update_time);