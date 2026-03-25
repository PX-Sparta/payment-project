package com.bootcamp.paymentdemo.domain.subscription.service;

import com.bootcamp.paymentdemo.domain.subscription.entity.Subscription;
import com.bootcamp.paymentdemo.domain.subscription.entity.SubscriptionStatus;
import com.bootcamp.paymentdemo.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    // 체험 0 -> 구독 활성

    // 체험 x -> 결제 성공 -> 구독 활성

    // 체험 x -> 결제 실패 -> 결제 연체

    // 체험 x -> 결제 실패 -> 결제 성공 -> 구독 활성

    // 결제 연체 -> 결제 실패 -> 이용 종료

    // 결제 연체 -> 결제 성공 -> 구독 활성

    // 구독 활성 -> 취소 요청 -> CANCELED(해지됨)

    // 구독 기간 종료 -> 이용 종료


}
