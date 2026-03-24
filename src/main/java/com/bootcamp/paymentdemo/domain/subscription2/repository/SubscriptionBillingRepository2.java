package com.bootcamp.paymentdemo.domain.subscription2.repository;

import com.bootcamp.paymentdemo.domain.subscription2.entity.BillingStatus2;
import com.bootcamp.paymentdemo.domain.subscription2.entity.Subscription2;
import com.bootcamp.paymentdemo.domain.subscription2.entity.SubscriptionBilling2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionBillingRepository2 extends JpaRepository<SubscriptionBilling2, Long> {
    boolean existsBySubscriptionAndStatusInAndCreatedAtAfter(
            Subscription2 subscription,
            List<BillingStatus2> statuses,
            LocalDateTime after
    );

    boolean existsBySubscriptionAndScheduledDate(Subscription2 subscription, LocalDateTime scheduledDate);

    boolean existsByCustomerIdAndPlanIdAndScheduledDate(Long customerId, Long planId, LocalDateTime today);

    Optional<SubscriptionBilling2> findByPaymentId(String paymentId);
}
