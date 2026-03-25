package com.bootcamp.paymentdemo.domain.subscription.entity;

import com.bootcamp.paymentdemo.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 구독 정보
@Entity
@Table(name = "plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String planName;



    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingInterval billingInterval;

    private String description;

    private String content;

    public Plan(
            String planName,
            Integer price,
            BillingInterval billingInterval,
            String description,
            String content
    ) {
        this.planName = planName;
        this.price = price;
        this.billingInterval = billingInterval;
        this.description = description;
        this.content=content;
    }
}
