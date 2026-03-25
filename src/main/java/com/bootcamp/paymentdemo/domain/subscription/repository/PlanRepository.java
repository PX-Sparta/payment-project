package com.bootcamp.paymentdemo.domain.subscription.repository;

import com.bootcamp.paymentdemo.domain.subscription.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
