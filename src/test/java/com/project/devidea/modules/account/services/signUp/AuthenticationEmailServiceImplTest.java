package com.project.devidea.modules.account.services.signUp;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.environment.Environment;
import com.project.devidea.modules.environment.EnvironmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationEmailServiceImplTest {

    @Mock AccountRepository accountRepository;
    @Mock EnvironmentRepository environmentRepository;
    @InjectMocks AuthenticationEmailServiceImpl authenticationEmailService;

    @Test
    void 메일인증() throws Exception {

        // given
        Account account = mock(Account.class);
        String email = "email";
        String token = "token";
        when(accountRepository.findByEmail(email))
                .thenReturn(Optional.of(account));
        String url = "url";
        Environment environment = mock(Environment.class);
        when(environmentRepository.findByDescription("FRONT"))
                .thenReturn(environment);
        when(environment.getUrl())
                .thenReturn(url);

        // when
        authenticationEmailService.authenticateEmailToken(email, token);

        // then
        verify(accountRepository).findByEmail(email);
        verify(account).validateToken(token);
        verify(environmentRepository).findByDescription("FRONT");
        verify(environment).getUrl();
    }
}