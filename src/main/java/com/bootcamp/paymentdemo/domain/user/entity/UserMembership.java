package com.bootcamp.paymentdemo.domain.user.entity;

import com.bootcamp.paymentdemo.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "user_memberships")
public class UserMembership extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // 유저 한 명당 하나의 멤버십 상태
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity2 user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_grade_policy_id", nullable = false)
    private MembershipGradePolicy gradePolicy;

    @Column(nullable = false)
    private Long totalPaidAmount = 0L; // 누적 결제액 (등급 산정 기준)

    @Column(nullable = false)
    private Double currentPointRate; // 현재 적용 중인 적립률 (Snapshot)

    // [시니어의 Tip] 등급 업데이트 로직
    public void updateMembership(MembershipGradePolicy newPolicy, Long newTotalAmount) {
        this.gradePolicy = newPolicy;
        this.totalPaidAmount = newTotalAmount;
        this.currentPointRate = newPolicy.getPointRate();
    }
}
