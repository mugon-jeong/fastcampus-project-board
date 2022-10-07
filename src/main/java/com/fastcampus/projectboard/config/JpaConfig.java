package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(
                SecurityContextHolder.getContext()) // 시큐리티 정보를 모두 가지고 있음
            .map(SecurityContext::getAuthentication) // 인증 정보
            .filter(Authentication::isAuthenticated) // 인증 확인
            .map(Authentication::getPrincipal) // 인증한 유저 정보 (UserDetailsService interface 타입)
            .map(BoardPrincipal.class::cast) // BoardPrincipal로 타입캐스팅
            .map(BoardPrincipal::getUsername);
    }

}
