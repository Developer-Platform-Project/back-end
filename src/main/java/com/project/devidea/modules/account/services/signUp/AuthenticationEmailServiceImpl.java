package com.project.devidea.modules.account.services.signUp;

import com.project.devidea.infra.error.exception.ErrorCode;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.exception.AccountException;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.environment.EnvironmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationEmailServiceImpl implements AuthenticationEmailService {

    private final AccountRepository accountRepository;
    private final EnvironmentRepository environmentRepository;

    @Override
    public String authenticateEmailToken(String email, String token) {
        Account account = accountRepository.findByEmail(email).orElseThrow(
                () -> new AccountException("회원을 찾을 수 없습니다.", ErrorCode.ACCOUNT_ERROR));
        account.validateToken(token);
        return environmentRepository.findByDescription("FRONT").getUrl();
    }
}
