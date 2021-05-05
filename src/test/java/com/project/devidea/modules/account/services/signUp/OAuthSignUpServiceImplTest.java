package com.project.devidea.modules.account.services.signUp;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthSignUpServiceImplTest {

    @Mock BCryptPasswordEncoder passwordEncoder;
    @Mock AccountRepository accountRepository;
    private final String OAUTH_PASSWORD = "dev_idea_oauth_password";
    @InjectMocks OAuthSignUpServiceImpl oAuthSignUpService;

    @Test
    void 회원가입_OAuth() throws Exception {

        try (MockedStatic<Account> staticAccount = mockStatic(Account.class)) {
            // given
            SignUp.OAuthRequest request = mock(SignUp.OAuthRequest.class);
            Account account = mock(Account.class);
            staticAccount.when(() -> Account.createAccount(request, passwordEncoder, OAUTH_PASSWORD))
                    .thenReturn(account);
            when(accountRepository.save(account)).thenReturn(account);

            // when
            SignUp.Response response = oAuthSignUpService.signUp(request);

            // then
            staticAccount.verify(() -> Account.createAccount(request, passwordEncoder, OAUTH_PASSWORD));
            verify(account).generateEmailToken();
            verify(accountRepository).save(account);
            assertNotNull(response);
        }
    }
}