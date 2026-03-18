package com.bootcamp.paymentdemo.domain.user.service;


import com.bootcamp.paymentdemo.domain.user.entity.MembershipGradePolicy;
import com.bootcamp.paymentdemo.domain.user.entity.UserEntity2;
import com.bootcamp.paymentdemo.domain.user.entity.UserMembership;
import com.bootcamp.paymentdemo.domain.user.repository.MembershipGradePolicyRepository;
import com.bootcamp.paymentdemo.domain.user.repository.UserMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final UserMembershipRepository userMembershipRepository;
    private final MembershipGradePolicyRepository policyRepository;

    @Transactional
    public void createDefaultMembership(UserEntity2 user) {
        //1. 기본 등급(NORMAL) 정책을 찾아옴.
        MembershipGradePolicy defaultPolicy = policyRepository.findByGradeCode("NORMAL")
                .orElseThrow(() -> new IllegalStateException("없는 등급니다."));

        // 반드시 common error 수정해서 넣어야함,

        //2. 유저와 연결된 멤버십 레코드를 생성함.
        UserMembership membership = UserMembership.builder()
                .user(user)
                .gradePolicy(defaultPolicy)
                .totalPaidAmount(0L)
                .currentPointRate(defaultPolicy.getPointRate())
                .build();

        userMembershipRepository.save(membership);


    }

    // 등급이 바뀌는 메서드
    @Transactional
    public void updateMembershipAfterPayment(Long userId, Long addedAmount) {

        // 1. 유저의 멤버십 정보 조회
        UserMembership membership = userMembershipRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("멤버십 정보를 찾을 수 없습니다."));

        // 2. 누적 금액 갱신
        Long newTotalAmount = membership.getTotalPaidAmount() + addedAmount;

        // 3. 바뀐 금액에 따른 정책 조회 및 등급 변경
        MembershipGradePolicy newPolicy = policyRepository.findSuitablePolicy(newTotalAmount)
                .orElse(membership.getGradePolicy()); // 없으면 현재 유지

        // 4. 멤버십 업데이트 DirtyChecking
        membership.updateMembership(newPolicy, newTotalAmount);


    }


}
