package com.bootcamp.paymentdemo.domain.payment.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포트원 V2 웹훅 데이터를 수신하기 위한 DTO
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 하위호환성 방어: 모르는 필드는 무시
public class PortOneWebhookRequest {

    private String type;       // 이벤트 타입 (예: "Transaction.Paid")
    private String timestamp;  // 웹훅 발송 시간
    private WebhookData data;  // 실제 결제 핵심 데이터

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebhookData {
        private String paymentId;      // 포트원 결제 고유 번호 (가장 중요)
        private String transactionId;  // 트랜잭션 ID
        private String storeId;        // 상점 ID
    }
}
