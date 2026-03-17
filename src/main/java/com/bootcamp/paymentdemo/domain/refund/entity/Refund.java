package com.bootcamp.paymentdemo.domain.refund.entity;

import com.bootcamp.paymentdemo.domain.refund.enums.RefundStatus;
import com.bootcamp.paymentdemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "refunds",
        indexes = {
                @Index(name = "idx_refunds_payment_key_deleted_created", columnList = "payment_key,deleted_at,created_at")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE refunds SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Refund extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // PortOne에서 발급한 결제 고유 ID
        @Column(unique = true)
        private String paymentKey;

        @Column(nullable = false)
        private Long refundAmount;

        @Column(nullable = false)
        private String reason;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private RefundStatus status;

        @Column(nullable = false)
        private LocalDateTime processedAt;




}
