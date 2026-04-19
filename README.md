# creator-settlement-api
과제 B - 크리에이터 정산 API

크리에이터의 판매 내역과 환불 내역을 기반으로  
월별 정산 금액과 기간별 운영자 집계를 계산하는 백엔드 API입니다.

Spring Boot(Java), JPA, H2를 사용해 구현했으며,  
샘플 데이터를 애플리케이션 시작 시 자동 주입하여  
정산 시나리오를 바로 검증할 수 있도록 구성했습니다.

---

## 📌 프로젝트 개요

이 프로젝트는 크리에이터가 강의를 판매하고,  
플랫폼이 판매 금액에서 수수료를 제외한 금액을 정산하는 과정을 다룹니다.

주요 기능은 다음과 같습니다.

- 판매 내역 목록 조회
- 환불 내역 목록 조회
- 크리에이터별 월별 정산 조회
- 운영자용 기간 집계 조회

---

## 🧰 기술 스택

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Validation
- H2 Database
- Lombok
- springdoc-openapi (Swagger UI)
- JUnit 5
- AssertJ

---

## 🚀 실행 방법

### 1. 테스트 실행

```bash
./gradlew test
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. H2 콘솔 접속
브라우저에서 아래 주소로 접속합니다.
```
http://localhost:8080/h2-console
```
접속 정보:

- JDBC URL: `jdbc:h2:mem:creator_settlement`
- User Name: `user`
- Password: `pwd`

### 4. Swagger UI 접속
브라우저에서 아래 주소로 접속합니다.
```
http://localhost:8080/swagger-ui.html
```
---
## 📚 API 목록 및 예시

### 1) 판매 내역 조회

크리에이터별 판매 내역을 기간 기준으로 조회합니다.
- 요청 (creator-1 크리에이터 / 2025-03-01 ~ 2025-03-31)

```http
GET /sales?creatorId=creator-1&startDate=2025-03-01&endDate=2025-03-31
```
- 응답
```json
[
  {
    "saleRecordId": "sale-4",
    "courseId": "course-2",
    "courseTitle": "JPA 실전",
    "studentId": "student-4",
    "amount": 80000,
    "paidAt": "2025-03-22T02:00:00"
  },
  {
    "saleRecordId": "sale-3",
    "courseId": "course-2",
    "courseTitle": "JPA 실전",
    "studentId": "student-3",
    "amount": 80000,
    "paidAt": "2025-03-20T00:00:00"
  },
  {
    "saleRecordId": "sale-2",
    "courseId": "course-1",
    "courseTitle": "Spring Boot 입문",
    "studentId": "student-2",
    "amount": 50000,
    "paidAt": "2025-03-15T05:30:00"
  },
  {
    "saleRecordId": "sale-1",
    "courseId": "course-1",
    "courseTitle": "Spring Boot 입문",
    "studentId": "student-1",
    "amount": 50000,
    "paidAt": "2025-03-05T01:00:00"
  }
]
```

### 2) 환불 내역 조회

크리에이터별 환불 내역을 기간 기준으로 조회합니다.
- 요청 (creator-1 크리에이터 / 2025-03-01 ~ 2025-03-31)

```http
GET /refunds?creatorId=creator-1&startDate=2025-03-01&endDate=2025-03-31
```
- 응답
```json
[
  {
    "refundRecordId": "cancel-2",
    "saleRecordId": "sale-4",
    "courseId": "course-2",
    "courseTitle": "JPA 실전",
    "studentId": "student-4",
    "refundAmount": 30000,
    "refundedAt": "2025-03-28T06:00:00"
  },
  {
    "refundRecordId": "cancel-1",
    "saleRecordId": "sale-3",
    "courseId": "course-2",
    "courseTitle": "JPA 실전",
    "studentId": "student-3",
    "refundAmount": 80000,
    "refundedAt": "2025-03-25T01:00:00"
  }
]
```

### 3) 크리에이터 월별 정산 조회

크리에이터 ID와 조회 연월을 기준으로 월별 정산 결과를 조회합니다.
- 요청 (creator-1 크리에이터 / 2025-03)
```http
GET /settlements/monthly?creatorId=creator-1&yearMonth=2025-03
```
- 응답
```json
{
  "creatorId": "creator-1",
  "creatorName": "김강사",
  "yearMonth": "2025-03",
  "totalSalesAmount": 260000,
  "refundAmount": 110000,
  "netSalesAmount": 150000,
  "feeRatePercent": 20,
  "feeAmount": 30000,
  "payoutAmount": 120000,
  "salesCount": 4,
  "refundCount": 2
}
```

### 4) 운영자용 기간 집계 조회

기간 내 전체 크리에이터의 정산 현황과 전체 합계를 조회합니다.
- 요청 (2025-03-01 ~ 2025-03-31)
```http
GET /admin/settlements?startDate=2025-03-01&endDate=2025-03-31
```
- 응답
```json
{
  "periodStart": "2025-03-01",
  "periodEnd": "2025-03-31",
  "items": [
    {
      "creatorId": "creator-1",
      "creatorName": "김강사",
      "totalSalesAmount": 260000,
      "refundAmount": 110000,
      "netSalesAmount": 150000,
      "feeAmount": 30000,
      "payoutAmount": 120000,
      "salesCount": 4,
      "refundCount": 2
    },
    {
      "creatorId": "creator-2",
      "creatorName": "이강사",
      "totalSalesAmount": 60000,
      "refundAmount": 0,
      "netSalesAmount": 60000,
      "feeAmount": 12000,
      "payoutAmount": 48000,
      "salesCount": 1,
      "refundCount": 0
    },
    {
      "creatorId": "creator-3",
      "creatorName": "박강사",
      "totalSalesAmount": 0,
      "refundAmount": 0,
      "netSalesAmount": 0,
      "feeAmount": 0,
      "payoutAmount": 0,
      "salesCount": 0,
      "refundCount": 0
    }
  ],
  "totalPayoutAmount": 168000
}
```

---
## 🧱 데이터 모델 (DB / ERD)

필수 구현 기준으로 아래 4개 테이블을 사용했습니다.

| 테이블명 | 주요 역할 | 핵심 컬럼 | 설명 |
|---|---|---|---|
| `Creator` | 크리에이터 기본 정보 저장 | `id`, `name` | 정산의 최상위 기준이 되는 주체를 저장합니다. |
| `Course` | 강의 정보 저장 | `id`, `creator_id`, `title` | 크리에이터가 개설한 강의를 저장하며, `Creator`와 연결됩니다. |
| `SaleRecord` | 판매 내역 저장 | `id`, `course_id`, `student_id`, `amount`, `paidAt` | 결제 완료 시점을 기준으로 정산 대상이 되는 판매 데이터를 저장합니다. |
| `RefundRecord` | 환불/취소 내역 저장 | `id`, `sale_record_id`, `refund_amount`, `refundedAt` | 원본 판매 내역을 참조하며, 취소 시점을 기준으로 정산에 반영됩니다. |

### DB 스키마 / ERD 개요

```text
Creator 1 ─── N Course 1 ─── N SaleRecord 1 ─── N RefundRecord
```

### 정산 기준

- 판매: `paidAt` 기준
- 환불: `refundedAt` 기준
- 월 경계: KST 기준 해당 월 1일 00:00:00 ~ 말일 23:59:59

---

## 🧠 요구사항 해석 및 가정

### 판매 내역 등록 / 취소 내역 등록

샘플 데이터를 애플리케이션 시작 시 자동 주입하는 방식으로 대체했습니다.

### 판매 내역 목록 조회

크리에이터별, 기간 필터를 적용해 조회 기능을 구현했습니다.

### 정산 금액 계산

- 총 판매 금액 = 해당 월 판매 합계
- 환불 금액 = 해당 월 환불 합계
- 순 판매 금액 = 총 판매 - 환불
- 플랫폼 수수료 = 순 판매의 20%
- 정산 예정 금액 = 순 판매 - 수수료

### 운영자 집계

기간 내 전체 크리에이터의 정산 예정 금액 목록과 전체 합계를 반환하도록 구현했습니다.

---

## 🏗️ 설계 결정과 이유

### 판매와 환불을 분리한 이유

판매와 환불은 기준 시점이 다르기 때문에 별도 엔티티로 분리했습니다.  
이를 통해 월 경계와 부분 환불 케이스를 명확하게 처리할 수 있었습니다.

### 서비스와 컨트롤러를 분리한 이유

- Controller: 요청/응답 처리
- Service: 정산 및 조회 로직
- Repository: DB 조회

역할을 분리해 가독성과 유지보수성을 높였습니다.

### 샘플 데이터를 시작 시 자동 주입한 이유

정산 계산을 바로 검증할 수 있도록,  
실행 시 기본 시나리오를 자동으로 재현하도록 구성했습니다.

### Swagger를 적용한 이유

API 테스트와 확인이 편하도록 `springdoc-openapi` 기반 Swagger UI를 사용했습니다.

---

## 🧪 테스트 실행 방법

테스트는 `src/test/java` 아래에서 작성했으며,  
정산 계산의 정확성을 중심으로 검증했습니다.

실행 명령어:

```bash
./gradlew test
```

### 검증한 주요 시나리오

- creator-1의 2025-03 정산
- 부분 환불 처리
- 월 경계 취소 반영
- creator-3의 빈 월 조회

---

## ⚠️ 미구현 / 제약사항

필수 요구사항 중심으로 구현했으며,  
다음 항목은 선택 구현 또는 확장 가능 영역으로 남겨두었습니다.

- 정산 확정 상태 관리
- 동일 기간 중복 정산 방지
- 정산 내역 CSV / 엑셀 다운로드
- 수수료율 변경 이력 관리

---

## 🤖 AI 활용 범위

초반 요구사항 해석과 설계 정리부터, 구현 방향 구상, 테스트 시나리오 구성, README 문서 작성까지 전 과정에서 AI를 보조 도구로 활용했습니다.
