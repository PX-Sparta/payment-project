package com.bootcamp.paymentdemo.domain.subscription.controller;

import com.bootcamp.paymentdemo.config.CustomUser;
import com.bootcamp.paymentdemo.domain.subscription.dto.request.CreateBillingRequest;
import com.bootcamp.paymentdemo.domain.subscription.dto.request.SubscriptionRequest;
import com.bootcamp.paymentdemo.domain.subscription.dto.response.BillingHistoryResponse;
import com.bootcamp.paymentdemo.domain.subscription.dto.response.CreateBillingResponse;
import com.bootcamp.paymentdemo.domain.subscription.dto.response.PlanResponse;
import com.bootcamp.paymentdemo.domain.subscription.dto.response.SubscriptionResponse;
import com.bootcamp.paymentdemo.domain.subscription.dto.response.SubscriptionStatusResponse;
import com.bootcamp.paymentdemo.domain.subscription.entity.SubscriptionPlan;
import com.bootcamp.paymentdemo.domain.subscription.entity.SubscriptionStatus;
import com.bootcamp.paymentdemo.domain.subscription.service.SubscriptionService;
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
//@RequestMapping("/api/subscriptions/v1")
@RequiredArgsConstructor
public class SubscriptionController {

}
