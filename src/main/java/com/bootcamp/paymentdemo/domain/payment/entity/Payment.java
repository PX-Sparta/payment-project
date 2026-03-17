package com.bootcamp.paymentdemo.domain.payment.entity;

import com.bootcamp.paymentdemo.domain.order.entity.Entity;
import com.bootcamp.paymentdemo.domain.payment.enums.PaymentStatus;
import com.bootcamp.paymentdemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@jakarta.persistence.Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payments_order_deleted_created", columnList = "order_id,deleted_at,created_at")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payments SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Payment extends BaseEntity {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(nullable = false)
        private Long orderId;

        // PortOne에서 발급한 결제 고유 ID
        @Column(unique = true)
        private String paymentKey;

        @Column(nullable = false)
        private Long amount;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private PaymentStatus status;

        @Column
        private LocalDateTime paidAt;
        @Column
        private LocalDateTime refundedAt;

        public static Payment create(Long order, String paymentKey, Long amount) {
                if (order == null) {
                        throw new IllegalArgumentException("Order cannot be null");
                }
                Payment payment = new Payment();
                payment.orderId = order;
                payment.paymentKey = paymentKey;
                payment.amount = amount;
                payment.status = PaymentStatus.READY;
                return payment;
        }

        public void confirm() {
                this.status = PaymentStatus.PAID;
                this.paidAt = LocalDateTime.now();
        }

        public void fail() {
                this.status = PaymentStatus.FAILED;
        }


        public boolean isRefundable() {
                return this.status == PaymentStatus.PAID;
        }

        public boolean isAlreadyProcessed() {
                return this.status == PaymentStatus.PAID || this.status == PaymentStatus.FAILED;
        }


}
