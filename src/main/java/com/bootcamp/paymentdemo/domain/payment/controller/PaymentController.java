package com.bootcamp.paymentdemo.domain.payment.controller;

import com.bootcamp.paymentdemo.domain.payment.dto.Request.PortOneWebhookRequest;
import com.bootcamp.paymentdemo.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 포트원 결제 결과 웹훅 수신 엔드포인트
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handlePortOneWebhook(
            @RequestHeader(value = "webhook-id", required = false) String webhookId,
            @RequestBody PortOneWebhookRequest request) {

        try {
            log.info(" 포트원 웹훅 수신 WebhookId: {}, EventType: {}, PaymentId: {}",
                    webhookId, request.getType(), request.getData().getPaymentId());

            // 비즈니스 로직(Service)으로 검증 및 상태 업데이트 위임
            paymentService.processWebhook(webhookId, request);

            // 정상 처리 시 포트원에 "성공적으로 수신함" 응답 (재시도 중단)
            return ResponseEntity.ok("Webhook Received");

        } catch (IllegalArgumentException e) {
            // 위조된 결제 등 비즈니스 규칙 위반 -> 더 이상 재시도 불필요
            log.warn("웹훅 처리 거부 (비즈니스 오류): {}", e.getMessage());
            return ResponseEntity.ok("Webhook Rejected");
        } catch (Exception e) {
            // DB 통신 장애 등 일시적 오류 -> 500 에러를 뱉어서 포트원이 나중에 재시도하게 함
            log.error("웹훅 처리 중 시스템 에러 발생: ", e);
            return ResponseEntity.internalServerError().body("Webhook Processing Failed");
        }
    }

}
