package com.fastcampus.projectboard.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import java.util.Optional;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    // security layer에서 userDetailsService를 부를때 사용되기 때문에 선언
    @MockBean
    private UserAccountRepository userAccountRepository;

    // 테스트용 계정 생성
    @BeforeTestMethod // 스프링테스트를 할때만 사용할 예정일때 사용
    public void securitySetUp() {
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
            "unoTest",
            "pw",
            "uno-test@email.com",
            "uno-test",
            "test memo"
        )));
    }

}
