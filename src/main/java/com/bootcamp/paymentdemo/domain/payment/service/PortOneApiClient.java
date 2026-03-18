package com.bootcamp.paymentdemo.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortOneApiClient {

    // 외부 API 호출을 도와주는 스프링의 기본 도구입니다.
    private final RestTemplate restTemplate = new RestTemplate();

    // TODO: application.yml에서 포트원 V2 Secret Key를 주입받아야 합니다.
    private final String PORTONE_API_SECRET = "여기에_포트원_시크릿키를_주입";

    /**
     * 포트원 V2 단건 결제 조회 API 호출
     */
    public String getPaymentInfo(String paymentId) {
        String url = "https://api.portone.io/payments/" + paymentId;

        // 1. 헤더 세팅 (인증 키 및 멱등 키)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + PORTONE_API_SECRET);
        // 포트원 문서에서 요구한 대로 멱등 키(Idempotency-Key)를 쌍따옴표로 감싸서 생성
        headers.set("Idempotency-Key", "\"" + UUID.randomUUID().toString() + "\"");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 2. 포트원에 GET 요청 쏘기
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            log.info("포트원 결제 조회 성공: {}", response.getBody());
            // 실제로는 String이 아니라 PortOnePaymentReceipt 같은 전용 DTO로 받아야 함.
            return response.getBody();

        } catch (Exception e) {
            log.error("포트원 결제 내역 조회 실패: {}", e.getMessage());
            throw new IllegalArgumentException("포트원 결제 조회 중 오류 발생");
        }
    }

    /**
     * 포트원 V2 빌링키(정기결제) 자동 결제 요청 API
     * * @param billingKey 포트원에서 발급받아 DB에 저장해둔 빌링키
     *
     * @param paymentId 이번 결제의 고유 식별자 (우리가 생성해서 보냄)
     * @param amount    결제할 금액
     * @return 결제 성공 여부
     */
    public boolean payWithBillingKey(String billingKey, String paymentId, int amount) {
        // 포트원 V2 빌링키 결제 엔드포인트 (포트원 문서 규격)
        String url = "https://api.portone.io/payments/" + paymentId + "/billing-key";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + PORTONE_API_SECRET);
        headers.set("Idempotency-Key", "\"" + UUID.randomUUID().toString() + "\"");
        headers.set("Content-Type", "application/json");

        // 요청 바디 생성 (포트원 문서 기준 필수 값)
        // 실무처럼 하려면 Map 대신 별도의 Request DTO 클래스를 구현하는 쪽이 좋습니다. 이건 구현을 위해 임시로 작성
        String requestBody = String.format(
                "{\"billingKey\": \"%s\", \"orderName\": \"월간 정기 구독\", \"amount\": {\"total\": %d}}",
                billingKey, amount
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("빌링키 결제 요청 시작 - PaymentId: {}", paymentId);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);

            log.info("빌링키 결제 성공! 응답: {}", response.getBody());
            return true; // 정상적으로 결제됨 (상태코드 2xx)

        } catch (Exception e) {
            // 한도 초과, 잔액 부족, 카드 정지 등의 이유로 결제가 실패하면 이쪽으로 빠집니다.
            log.error("빌링키 결제 실패 (잔액 부족 등): {}", e.getMessage());
            return false;
        }

    }
}