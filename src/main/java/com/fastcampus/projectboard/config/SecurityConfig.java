package com.fastcampus.projectboard.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.dto.security.KakaoOauth2Response;
import com.fastcampus.projectboard.service.UserAccountService;
import java.util.UUID;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {

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
                   .formLogin(withDefaults())
                   .logout(logout -> logout.logoutSuccessUrl("/"))
                   .oauth2Login(oauth -> oauth
                       .userInfoEndpoint(userInfo -> userInfo
                           .userService(oAuth2UserService)
                       )
                   )
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
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return username -> userAccountService.searchUser(username)
                                             .map(BoardPrincipal::from)
                                             .orElseThrow(
                                                 () -> new UsernameNotFoundException(
                                                     "유저를 찾을 수 없습니다. - username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
        UserAccountService userAccountService,
        PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            KakaoOauth2Response kakaoResponse = KakaoOauth2Response.from(
                oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration()
                                               .getRegistrationId(); // -> "kakao" yml 설정에 있는 값
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;
            // createDelegatingPasswordEncoder를 쓸때 {} 안에 어떤 알고리즘을 쓸지 명시
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());
            return userAccountService.searchUser(username)
                                     .map(BoardPrincipal::from)
                                     .orElseGet(
                                         () -> BoardPrincipal.from(userAccountService.saveUser(
                                             username, dummyPassword, kakaoResponse.email(),
                                             kakaoResponse.nickname(), null
                                         )));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 패스워드 인코더 설정은 위윔해서 가지고 오겠다.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
