package com.bootcamp.paymentdemo.domain.customer.repository;

import com.bootcamp.paymentdemo.domain.customer.entity.MembershipRankPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MembershipGradePolicyRepository extends JpaRepository<MembershipRankPolicy, Long> {

    // 1. 회원가입 시 기본 등급 조회를 위해 사용
    Optional<MembershipRankPolicy> findByRankCode(String gradeCode);

    // 2. [핵심] 누적 금액에 맞는 정책 조회 (BETWEEN 활용)
    @Query("SELECT p FROM MembershipRankPolicy p " +
            "WHERE : totalAmount BETWEEN p.minPaidAmount AND p.maxPaidAmount " +
            "AND p.isActive = true")
    Optional<MembershipRankPolicy> findSuitablePolicy(@Param("totalAmount") Long totalAmount);

    @Query(" SELECT p From MembershipRankPolicy p " +
           "WHERE :amount BETWEEN p.minPaidAmount AND p.maxPaidAmount " +
           "AND p.isActive = true")
    Optional<MembershipRankPolicy> findByAmountInRange(@Param("amount") Long amount);

    Optional<MembershipRankPolicy> findTopByMinPaidAmountLessThanEqualOrderByMinPaidAmountDesc(Long amount);
}
