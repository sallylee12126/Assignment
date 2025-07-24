# CoinDesk API Spring Boot Project

## 專案需求

- Maven
- JDK 8
- Spring Boot
- H2 Database (with Spring Data JPA)
- 呼叫 CoinDesk API 並轉換資料
- 幣別中英文對照資料表 CRUD
- 完整單元與整合測試

## 專案結構
```
CoinDesk-API/
├── Assignment.Cathay.json             # Postman 測試腳本
├── coindesk-api/                      # Spring Boot 專案根目錄
│   ├── pom.xml                        # Maven 設定檔
│   ├── README.md                      # 專案說明
│   ├── create_gitignore.sh            # 自動生成 .gitignore 腳本
│   ├── target/                        # Maven 編譯輸出目錄
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/cathay/coindesk/
│       │   │       ├── api/data/           # API 資料模型
│       │   │       ├── constant/           # 常數定義
│       │   │       ├── controller/         # REST 控制器
│       │   │       ├── dto/                # 資料傳輸物件
│       │   │       ├── error/              # 錯誤模型
│       │   │       ├── exception/          # 自訂 Exception
│       │   │       ├── persistence/entity/ # JPA 實體類別
│       │   │       ├── repository/         # JPA Repository 介面
│       │   │       ├── rest/               # 共用回應格式 (RestResult, RestStatus)
│       │   │       ├── service/            # 服務邏輯 (CoindeskService, CurrencyService)
│       │   │       ├── utils/              # 工具類別
│       │   │       ├── validator/          # 驗證邏輯
│       │   │       └── CoindeskApiApplication.java # 主啟動類
│       │   └── resources/
│       │       ├── application.yml         # Spring Boot 設定
│       │       ├── data.sql                # 初始化資料
│       │       └── schema.sql              # 資料庫結構
│       └── test/                           # 單元與整合測試
└── .gitignore
```

## CoinDesk JSON 範例

```json
{
  "time": {
    "updated": "Sep 2, 2024 07:07:20 UTC",
    "updatedISO": "2024-09-02T07:07:20+00:00",
    "updateduk": "Sep 2, 2024 at 08:07 BST"
  },
  "disclaimer": "just for test",
  "chartName": "Bitcoin",
  "bpi": {
    "USD": {
      "code": "USD",
      "symbol": "&#36;",
      "rate": "57,756.298",
      "description": "United States Dollar",
      "rate_float": 57756.2984
    },
    "GBP": {
      "code": "GBP",
      "symbol": "&pound;",
      "rate": "43,984.02",
      "description": "British Pound Sterling",
      "rate_float": 43984.0203
    },
    "EUR": {
      "code": "EUR",
      "symbol": "&euro;",
      "rate": "52,243.287",
      "description": "Euro",
      "rate_float": 52243.2865
    }
  }
}
```


## Postman測試檔
- 已包含：`Assignment.Cathay.json`
- 可透過 Postman → Import → JSON 檔案，來載入所有 requests。