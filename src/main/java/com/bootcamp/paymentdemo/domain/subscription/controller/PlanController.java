package com.bootcamp.paymentdemo.domain.subscription.controller;

import com.bootcamp.paymentdemo.domain.subscription.dto.Response.PlanResponse;
import com.bootcamp.paymentdemo.domain.subscription.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping("/api/v1/plans")
    public ResponseEntity<List<PlanResponse>> getPlans() {
        return ResponseEntity.status(HttpStatus.OK).body(planService.planGetAll());
    }
}
