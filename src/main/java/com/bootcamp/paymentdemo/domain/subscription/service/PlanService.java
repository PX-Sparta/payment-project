package com.bootcamp.paymentdemo.domain.subscription.service;

import com.bootcamp.paymentdemo.domain.subscription.dto.Response.PlanResponse;
import com.bootcamp.paymentdemo.domain.subscription.entity.BillingInterval;
import com.bootcamp.paymentdemo.domain.subscription.entity.Plan;
import com.bootcamp.paymentdemo.domain.subscription.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    // 모두 조회
    @Transactional(readOnly = true)
    public List<PlanResponse> planGetAll() {
        List<Plan> plans = planRepository.findAll();

        List<PlanResponse> dtos = new ArrayList<>();
        for (Plan plan : plans) {
            PlanResponse dto = new PlanResponse(
                    String.valueOf(plan.getId()),
                    plan.getPlanName(),
                    plan.getPrice(),
                    plan.getBillingInterval() == BillingInterval.YEARLY ? "ANNUAL" : "MONTHLY"
            );
            dtos.add(dto);
        }
        return dtos;
    }
}
