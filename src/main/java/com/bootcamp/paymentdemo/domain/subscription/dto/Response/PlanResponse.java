package com.bootcamp.paymentdemo.domain.subscription.dto.Response;

import lombok.Getter;

@Getter
public class PlanResponse {

    private final String planId;
    private final String name;
    private final Integer amount;
    private final String billingCycle;

    public PlanResponse(String planId, String name, Integer amount, String billingCycle) {
        this.planId = planId;
        this.name = name;
        this.amount = amount;
        this.billingCycle = billingCycle;
    }
}
