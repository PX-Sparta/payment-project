package com.bootcamp.paymentdemo.domain.user.repository;

import com.bootcamp.paymentdemo.domain.user.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {

    //유저 ID로 현재 멤버십 상태 조회
    Optional<UserMembership> findByUserId(Long userId);
}
