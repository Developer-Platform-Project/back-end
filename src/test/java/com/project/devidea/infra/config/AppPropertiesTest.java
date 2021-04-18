package com.project.devidea.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AppPropertiesTest {

    @Autowired
    AppProperties appProperties;

    @Test
    void host_가져오기_확인() throws Exception {
        String host = appProperties.getHost();
        assertEquals(host, "http://192.168.20.2:8080");
    }

//    @Test
//    void 환경변수_가져오기() throws Exception {
//        String value = System.getenv("FRONT_END_URL");
//        log.info("{}", value);
//        assertEquals(value, "http://localhost:3000");
//    }
}