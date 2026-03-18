package com.bootcamp.paymentdemo.domain.subscription.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    // private final SubscriptionRepository subscriptionRepository;
    // private final PortOneApiClient portOneApiClient;

    /**
     * 매일 자정(00:00:00)에 실행되는 정기결제 스케줄러
     * cron = "초 분 시 일 월 요일"
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void processDailySubscriptions() {
        log.info(" [스케줄러 시작] 매일 자정 정기결제 배치가 실행되었습니다. Time: {}", LocalDateTime.now());

        try {
            // TODO: [도메인 담당자 작업]
            // 1. Subscription 테이블에서 '상태가 ACTIVE' 이고 '현재 이용 기간 종료일 <= 오늘'인 구독 목록을 조회하세요.
            // List<Subscription> dueSubscriptions = subscriptionRepository.findDueSubscriptions(LocalDate.now());

            // 2. 조회된 구독 목록을 돌면서 결제 실행
            /*
            for (Subscription sub : dueSubscriptions) {
                String billingKey = sub.getBillingKey();
                int amount = sub.getPlan().getPrice();
                String paymentId = "sub_" + UUID.randomUUID().toString(); // 고유 결제 ID 생성

                log.info("구독 결제 시도 - SubscriptionId: {}, Amount: {}", sub.getId(), amount);

                // 3. 포트원 빌링키 결제 API 호출 (인프라 담당자가 만들어둔 메서드 사용)
                // boolean isSuccess = portOneApiClient.payWithBillingKey(billingKey, paymentId, amount);

                // 4. 결제 결과에 따라 구독 상태 및 다음 결제일(endedAt) 업데이트
                if (isSuccess) {
                    // 성공 처리 로직...
                } else {
                    // 실패 처리 (미납 상태로 변경 등)...
                }
            }
            */
            log.info(" [스케줄러 종료] 정기결제 배치가 무사히 완료되었습니다.");

        } catch (Exception e) {
            log.error(" [스케줄러 에러] 정기결제 배치 실행 중 치명적 오류 발생: {}", e.getMessage(), e);
        }
    }
}
