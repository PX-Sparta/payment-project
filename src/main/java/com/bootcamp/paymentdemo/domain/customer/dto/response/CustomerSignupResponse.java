package com.bootcamp.paymentdemo.domain.customer.dto.response;

import lombok.Builder;

import java.util.Date;

@Builder
public record CustomerSignupResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        Date createdAt
) {
}
