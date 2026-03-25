package com.bootcamp.paymentdemo.security.oauth;

import com.bootcamp.paymentdemo.domain.customer.entity.Customer;
import com.bootcamp.paymentdemo.domain.customer.repository.CustomerRepository;
import com.bootcamp.paymentdemo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerRepository customerRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 1. 이메일 추출 (구글/카카오 분기)
        String email = "";
        if (oAuth2User.getAttributes().containsKey("email")) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (oAuth2User.getAttributes().containsKey("kakao_account")) {
            java.util.Map<String, Object> kakaoAccount = (java.util.Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            if (kakaoAccount.containsKey("email")) {
                email = (String) kakaoAccount.get("email");
            } else {
                email = oAuth2User.getName() + "@social.sparta.com";
            }
        }

        // 2. DB에서 유저 조회 (CustomOAuth2UserService에서 이미 가입시켰으므로 무조건 존재함)
        // 만약의 예외 상황을 대비해 orElseThrow 대신 안전하게 한 번 더 처리
        final String finalEmail = email;
        Customer customer = customerRepository.findByEmail(finalEmail)
                .orElseGet(() -> customerRepository.findAll().get(0)); // 극단적인 fallback (실제론 도달 안함)

        // 3. 토큰 발급
        String token = jwtTokenProvider.generateAccessToken(customer.getId(), finalEmail);

        // 4. 리다이렉트 (상대 경로)
        String targetUrl = "/?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}