package com.bootcamp.paymentdemo.domain.subscription2.controller;

import com.bootcamp.paymentdemo.config.CustomUser;
import com.bootcamp.paymentdemo.domain.subscription2.dto.request.SubscriptionRequest2;
import com.bootcamp.paymentdemo.domain.subscription2.dto.request.SubscriptionUpdateRequest;
import com.bootcamp.paymentdemo.domain.subscription2.dto.response.BillingHistoryResponse;
import com.bootcamp.paymentdemo.domain.subscription2.dto.response.PlanResponse2;
import com.bootcamp.paymentdemo.domain.subscription2.dto.response.SubscriptionResponse2;
import com.bootcamp.paymentdemo.domain.subscription2.dto.response.SubscriptionStatusResponse;
import com.bootcamp.paymentdemo.domain.subscription2.entity.SubscriptionPlan2;
import com.bootcamp.paymentdemo.domain.subscription2.entity.SubscriptionStatus2;
import com.bootcamp.paymentdemo.domain.subscription2.service.SubscriptionService2;
import com.bootcamp.paymentdemo.global.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
// @RequestMapping
public class SubscriptionController2 {

    private final SubscriptionService2 subscriptionService;

    /**
     * [GET] 구독 플랜 목록 조회
     * 프론트엔드가 Config 없이도 바로 찾을 수 있게 /api/plans를 명시합니다.
     */
//    @GetMapping("/api/v1/plans")
//    public ResponseEntity<ApiResponse<List<PlanResponse2>>> getPlans() {
//        log.info(">>>> [API] 구독 플랜 목록 조회 (/api/plans)");
//        List<SubscriptionPlan2> plans = subscriptionService.getActivePlans();
//
//        List<PlanResponse2> response = plans.stream()
//                .map(p -> PlanResponse2.builder()
//                        .planId(String.valueOf(p.getId()))
//                        .name(p.getName())
//                        .amount(p.getPrice())
//                        .billingCycle(p.getInterval() != null ? p.getInterval().name() : "MONTHLY")
//                        .description(p.getDescription())
//                        .build())
//                .toList();
//
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }

    /**
     * [POST] 구독 신청
     */
    @PostMapping("/api/v1/subscriptions")
    public ResponseEntity<ApiResponse<Map<String, String>>> createSubscription(
            @AuthenticationPrincipal CustomUser user,
            @Valid @RequestBody SubscriptionRequest2 request) {

        Long subId = subscriptionService.initiateSubscription(user.getId(), request.getPlanId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Map.of("subscriptionId", String.valueOf(subId))));
    }

    /**
     * [GET] 구독 상세 조회
     */
    @GetMapping("/api/v1/subscriptions/{subscriptionId}")
    public ResponseEntity<ApiResponse<SubscriptionResponse2>> getSubscription(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(ApiResponse.success(
                subscriptionService.getSubscriptionDto(user.getId(), subscriptionId)
        ));
    }

    /**
     * [PATCH] 구독 상태 업데이트 (해지 등)
     * 규격: /api/v1/subscriptions/{subscriptionId}
     * 주의: 규격서에는 action 필드로 'cancel'을 받기로 되어 있습니다!
     */
    @PatchMapping("/api/v1/subscriptions/{subscriptionId}")
    public ResponseEntity<ApiResponse<SubscriptionStatusResponse>> updateSubscription(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long subscriptionId,
            @RequestBody SubscriptionUpdateRequest request) { // 👈 action('cancel')이 담긴 DTO

        if ("cancel".equals(request.getAction())) {
            subscriptionService.cancelSubscription(user.getId(), subscriptionId);
        }

        return ResponseEntity.ok(ApiResponse.success(
                new SubscriptionStatusResponse(subscriptionId, SubscriptionStatus2.CANCELED)
        ));
    }

    /**
     * [GET] 청구 내역 조회
     * 규격: /api/v1/subscriptions/{subscriptionId}/billings
     */
    @GetMapping("/api/v1/subscriptions/{subscriptionId}/billings")
    public ResponseEntity<ApiResponse<List<BillingHistoryResponse>>> getBillingHistory(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(ApiResponse.success(
                subscriptionService.getBillingHistoryDto(user.getId(), subscriptionId)
        ));
    }
}
