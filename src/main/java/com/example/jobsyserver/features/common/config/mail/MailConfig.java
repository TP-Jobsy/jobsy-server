package com.example.jobsyserver.features.common.config.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailConfigProperties.class)
@RequiredArgsConstructor
public class MailConfig {

    private final MailConfigProperties mailConfigProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfigProperties.host());
        mailSender.setPort(mailConfigProperties.port());
        mailSender.setUsername(mailConfigProperties.username());
        mailSender.setPassword(mailConfigProperties.password());

        Properties props = new Properties();
        props.putAll(mailConfigProperties.properties());
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}