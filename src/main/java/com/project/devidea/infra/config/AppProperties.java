package com.project.devidea.infra.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application-dev.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {

    private String host;
}
