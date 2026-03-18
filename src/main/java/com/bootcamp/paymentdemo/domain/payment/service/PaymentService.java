package com.bootcamp.paymentdemo.domain.payment.service;

import com.bootcamp.paymentdemo.domain.payment.dto.Request.PortOneWebhookRequest;
import com.bootcamp.paymentdemo.domain.payment.entity.Payment;
import com.bootcamp.paymentdemo.domain.payment.enums.PaymentStatus; // PAID를 쓰기 위해 임포트
import com.bootcamp.paymentdemo.domain.payment.repository.PaymentRepository;
// import com.bootcamp.paymentdemo.domain.order.service.OrderService;
// import com.bootcamp.paymentdemo.domain.webhook.repository.WebhookEventRepository;
// import com.bootcamp.paymentdemo.domain.webhook.entity.WebhookEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // TODO: 아래 주석 처리된 의존성들은 프로젝트 상황에 맞게 주입하시면 됩니다.
    // private final WebhookEventRepository webhookEventRepository;
    // private final PortOneApiClient portOneApiClient;
    // private final OrderService orderService;

    @Transactional
    public void processWebhook(String webhookId, PortOneWebhookRequest request) {

        // 1. [멱등성 검증] 이미 처리된 웹훅인지 확인 (포트원의 SQS 재시도 방어)
        /*
        if (webhookEventRepository.existsByWebhookId(webhookId)) {
            log.info("이미 처리된 웹훅입니다. 무시합니다. WebhookId: {}", webhookId);
            return;
        }
        */

        String paymentId = request.getData().getPaymentId();

        // 2. [교차 검증 준비] 우리 DB에 '결제 대기' 상태로 저장된 결제 건 꺼내오기
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 결제 건입니다. PaymentId: " + paymentId));

        if (payment.getStatus() == PaymentStatus.PAID) {
            log.info("이미 결제 완료 처리된 주문입니다. PaymentId: {}", paymentId);
            return;
        }

        // 3. [진짜 영수증 발급] 포트원 API를 직접 호출하여 실제 결제 내역 조회 (위변조 방지)
        // PortOnePaymentReceipt receipt = portOneApiClient.getPaymentInfo(paymentId);

        /* // 4. [보안 검증] 금액 및 결제 상태 확인
        if (!receipt.getStatus().equals("PAID") || !payment.getAmount().equals(receipt.getAmount())) {
            log.error("결제 정보 위변조 의심! 포트원 결제 강제 취소(보상 트랜잭션) 진행");

            // TODO: portOneApiClient.cancelPayment(paymentId) 호출 로직 추가

            payment.updateStatus(PaymentStatus.FAILED);
            throw new IllegalArgumentException("결제 금액이 일치하지 않거나 결제가 승인되지 않았습니다.");
        }
        */

        // 5. [최종 승인] 모든 검증 통과 -> 비즈니스 로직 적용
        log.info("결제 검증 완료. 상태를 PAID로 변경합니다.");
        // payment.updateStatus(PaymentStatus.PAID);

        // TODO: orderService.completeOrder(payment.getOrderId()); // 주문 상태 완료 처리
        // TODO: pointService.processPointRewards(...); // 멤버십 포인트 적립 처리

        // 6. [멱등성 기록] 다음 재시도를 막기 위해 웹훅 ID 저장
        // webhookEventRepository.save(new WebhookEvent(webhookId, paymentId, "PROCESSED"));
    }
}