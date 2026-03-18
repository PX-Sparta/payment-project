package com.bootcamp.paymentdemo.domain.payment.repository;

import com.bootcamp.paymentdemo.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 이 한 줄만 추가하시면 빨간 줄이 마법처럼 사라집니다!
    Optional<Payment> findByPaymentId(String paymentId);
}