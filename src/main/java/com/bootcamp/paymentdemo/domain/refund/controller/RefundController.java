package com.bootcamp.paymentdemo.domain.refund.controller;

import com.bootcamp.paymentdemo.domain.refund.dto.Response.RefundResponse;
import com.bootcamp.paymentdemo.domain.refund.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class RefundController {

    private final RefundService refundService;


    @PostMapping("/v1/payments/{paymentId}/cancel")
    public ResponseEntity<RefundResponse> cancelPayment(
            Authentication authentication,
            @PathVariable("paymentId") String paymentId) {
        RefundResponse response = refundService.cancel(authentication, paymentId);
        return ResponseEntity.ok(response);
    }
}
