package com.project.devidea.modules.account.services.signUp;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.event.SendEmailToken;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.environment.EnvironmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Qualifier("commonSignUpService")
public class CommonSignUpServiceImpl implements SignUpService {

    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher publisher;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public SignUp.Response signUp(SignUp.Request request) {

        Account account = Account.createAccount(request, passwordEncoder, "");
        account.generateEmailToken();
        accountRepository.save(account);

        publisher.publishEvent(SendEmailToken.builder()
                .token(account.getEmailCheckToken())
                .to(account.getEmail()).build());

        return modelMapper.map(account, SignUp.Response.class);
    }
}
