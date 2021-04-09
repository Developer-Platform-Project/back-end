package com.project.devidea.infra.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppPropertiesTest {

    @Autowired
    AppProperties appProperties;

    @Test
    void host_가져오기_확인() throws Exception {
        String host = appProperties.getHost();
        assertEquals(host, "http://localhost:8080");
    }
}