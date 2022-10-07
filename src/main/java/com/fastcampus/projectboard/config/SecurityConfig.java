package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .mvcMatchers(
                    HttpMethod.GET, //아래 url의 GET 요청은
                    "/",
                    "/articles",
                    "/articles/search-hashtag"
                ).permitAll() // 전부 허용
                .anyRequest().authenticated() // 다른 요청은 인증이 필요함
            )
            .formLogin().and()
            .logout()
            .logoutSuccessUrl("/")
            .and()
            .build();

    }

    // 아래의 방식은 스프링부트에서 추천하지 않고 HttpSecurity에서 permitAll로 대체를 권장
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // static, resource, css 같은 정적 리소스는 시큐리티의 감독 범위를 벗어나게 설정
//        return (web ->
//            web.ignoring().requestMatchers(
//                PathRequest.toStaticResources().atCommonLocations() // 보통 정적리소스들을 담는 위치
//            )
//        );
//    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
            .findById(username)
            .map(UserAccountDto::from)
            .map(BoardPrincipal::from)
            .orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다. - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 패스워드 인코더 설정은 위윔해서 가지고 오겠다.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
