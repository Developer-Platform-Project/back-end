package com.project.devidea.infra.config.security;


import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname)
                .orElse(accountRepository.findByNickname(emailOrNickname));
        if (account == null) {
            throw new UsernameNotFoundException("해당 메일로 가입되지 않았습니다.");
        }
        return new LoginUser(account);
    }
}
