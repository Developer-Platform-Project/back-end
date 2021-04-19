//package com.project.devidea.modules.account.event;
//
//import com.project.devidea.modules.account.AccountDummy;
//import com.project.devidea.modules.account.AccountService;
//import com.project.devidea.modules.account.dto.SignUp;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.event.ApplicationEvents;
//import org.springframework.test.context.event.RecordApplicationEvents;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@RecordApplicationEvents
//class AccountEventListenerTest {
//
//    @Autowired
//    AccountService accountService;
//    @Autowired
//    ApplicationEvents events;
//
//    @Test
//    void 인증메일_비동기_테스트() throws Exception {
//
////        given
//        SignUp.CommonRequest request = AccountDummy.getSignUpRequest();
//
////        when
//        accountService.signUp(request);
//
////        then
//        int count = (int) events.stream(SendEmailToken.class).count();
//        assertEquals(1, count);
//    }
//}