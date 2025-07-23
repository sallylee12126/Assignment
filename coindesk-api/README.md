# CoinDesk API Spring Boot Project

## 專案需求

- Maven
- JDK 8
- Spring Boot
- H2 Database (with Spring Data JPA)
- 呼叫 CoinDesk API 並轉換資料
- 幣別中英文對照資料表 CRUD
- 完整單元與整合測試

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
