package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.dto.SignUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class SignUpDetailControllerValidationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void 회원가입_상세정보_저장_토큰없음_경력년도가_음수인_경우() throws Exception {

//        given
        SignUp.DetailRequest failRequest = AccountDummy.getFailSignUpDetailRequestWithValid();

//        when, then
//        경력년도 음수, 토큰 없음
        mockMvc.perform(post("/sign-up/detail").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(2)));
    }
}
