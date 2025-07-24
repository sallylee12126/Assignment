-- 幣別主檔資料
INSERT INTO currency (code, chinese_name)
VALUES
    ('USD', '美元'),
    ('GBP', '英鎊'),
    ('EUR', '歐元'),
    ('JPY', '日幣'),
    ('TWD', '新台幣'),
    ('CNY', '人民幣'),
    ('HKD', '港幣'),
    ('KRW', '韓元'),
    ('SGD', '新加坡幣'),
    ('AUD', '澳幣');


-- 匯率記錄資料
-- INSERT INTO currency_rate (currency_id, update_time, rate)
-- VALUES (1, CURRENT_TIMESTAMP, 31.123456),
--        (2, CURRENT_TIMESTAMP, 39.654321),
--        (3, CURRENT_TIMESTAMP, 34.998877),
--        (4, CURRENT_TIMESTAMP, 0.219321),
--        (5, CURRENT_TIMESTAMP, 1.000000),
--        (6, CURRENT_TIMESTAMP, 4.513210),
--        (7, CURRENT_TIMESTAMP, 3.974100),
--        (8, CURRENT_TIMESTAMP, 0.023456),
--        (9, CURRENT_TIMESTAMP, 23.321000),
--        (10, CURRENT_TIMESTAMP, 21.876543);