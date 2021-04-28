package com.project.devidea.modules.account.service.signup;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.event.SendEmailToken;
import com.project.devidea.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonSignUpServiceImpl implements SignUpService {

    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher publisher;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public SignUp.Response signUp(SignUp.SignUpRequest request) {
        Account savedAccount = saveCommonAccount((SignUp.CommonRequest) request);
        publisher.publishEvent(SendEmailToken.builder()
                .token(savedAccount.getEmailCheckToken())
                .to(savedAccount.getEmail()).build());
        return modelMapper.map(savedAccount, SignUp.Response.class);
    }

    private Account saveCommonAccount(SignUp.CommonRequest request) {
        Account account = Account.createAccount(request, passwordEncoder);
        account.generateEmailToken();
        return accountRepository.save(account);
    }
}
