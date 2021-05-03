package com.project.devidea.modules.account.services.signUp;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.event.SendEmailToken;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.environment.Environment;
import com.project.devidea.modules.environment.EnvironmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonSignUpServiceImplTest {

    @Mock BCryptPasswordEncoder passwordEncoder;
    @Mock AccountRepository accountRepository;
    @Mock ApplicationEventPublisher publisher;
    @Mock ModelMapper modelMapper;
    @InjectMocks CommonSignUpServiceImpl commonSignUpService;

    @Test
    void 회원가입_일반() throws Exception {

        try (MockedStatic<Account> staticAccount = mockStatic(Account.class)) {
            // given
            SignUp.CommonRequest request = mock(SignUp.CommonRequest.class);
            Account account = mock(Account.class);
            staticAccount.when(() -> Account.createAccount(request, passwordEncoder, ""))
                    .thenReturn(account);
            when(accountRepository.save(account)).thenReturn(account);

            // when
            commonSignUpService.signUp(request);

            // then
            staticAccount.verify(() -> Account.createAccount(request, passwordEncoder, ""));
            verify(account).generateEmailToken();
            verify(accountRepository).save(account);
            verify(publisher).publishEvent(any(SendEmailToken.class));
            verify(modelMapper).map(account, SignUp.Response.class);
        }
    }
}