package com.project.devidea.infra;

import com.project.devidea.infra.config.security.CustomUserDetailService;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.services.signUp.CommonSignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import javax.persistence.EntityManager;

public class WithAccountFactory implements WithSecurityContextFactory<WithAccount> {
    @Autowired
    CommonSignUpServiceImpl accountServiceImpl;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    EntityManager entityManager;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        SignUp.CommonRequest signUpRequestDto = SignUp.CommonRequest.builder()
                .email(withAccount.NickName() + "@gmail.com")
                .name(withAccount.NickName())
                .nickname(withAccount.NickName())
                .password("password")
                .gender("남성")
                .passwordConfirm("yes")
                .build();
        accountServiceImpl.signUp(signUpRequestDto);
        entityManager.flush();
        UserDetails principal = customUserDetailService.loadUserByUsername(withAccount.NickName());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
