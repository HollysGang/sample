package com.hollysgang.sample.encryption.framework.module;

import com.hollysgang.sample.encryption.framework.core.PersonalInformationPointcutConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {
    @Bean
    public PersonalInformationPointcutConfig personalInformationPointcutConfig() {
        PersonalInformationPointcutConfig config = new PersonalInformationPointcutConfig();
        config.setEncryptionPointCut("execution(public * com.hollysgang.sample.encryption.demo.repository..*.*(..))");
        return config;
    }
}
