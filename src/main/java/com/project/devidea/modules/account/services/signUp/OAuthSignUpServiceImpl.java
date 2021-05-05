package com.project.devidea.modules.account.services.signUp;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Qualifier("oAuthSignUpService")
public class OAuthSignUpServiceImpl implements SignUpService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String OAUTH_PASSWORD = "dev_idea_oauth_password";

    @Override
    public SignUp.Response signUp(SignUp.Request request) {

        Account account = Account.createAccount(request, passwordEncoder, OAUTH_PASSWORD);
        account.generateEmailToken();
        accountRepository.save(account);

        return SignUp.Response.builder()
                .provider(account.getProvider())
                .id(account.getId().toString())
                .name(account.getName())
                .emailCheckToken(account.getEmailCheckToken())
                .build();
    }
}
