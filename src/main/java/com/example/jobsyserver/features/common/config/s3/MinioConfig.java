package com.example.jobsyserver.features.common.config.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.core.exception.SdkClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;


@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "s3", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({MinioConfigProperties.class, S3FeatureToggle.class})
public class MinioConfig {

    private final MinioConfigProperties props;

    @Value("classpath:readpolicy.json")
    private Resource bucketPolicy;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials creds = AwsBasicCredentials.create(props.accessKey(), props.secretKey());
        return S3Client.builder()
                .endpointOverride(URI.create(props.endpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .region(Region.of(props.region()))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    @Order(1)
    public ApplicationRunner initBucket(S3Client s3) {
        return args -> {
            String bucket = props.bucket();
            try {
                HeadBucketRequest head = HeadBucketRequest.builder()
                        .bucket(bucket)
                        .build();
                s3.headBucket(head);
                log.info("S3 бакет '{}' уже существует", bucket);
            } catch (NoSuchBucketException e) {
                log.info("Бакет '{}' не найден, создаём...", bucket);
                try {
                    CreateBucketRequest create = CreateBucketRequest.builder()
                            .bucket(bucket)
                            .build();
                    s3.createBucket(create);
                    log.info("S3 бакет '{}' успешно создан", bucket);
                } catch (S3Exception ex) {
                    log.error("Не удалось создать бакет '{}', пропускаем инициализацию", bucket, ex);
                }
            } catch (SdkClientException e) {
                log.warn("Не удалось подключиться к S3 по адресу '{}', пропускаем инициализацию бакета", props.endpoint());

            } catch (S3Exception e) {
                log.error("Ошибка при работе с S3 при инициализации бакета '{}'", bucket, e);
            }
        };
    }

    @Bean
    @Order(2)
    public ApplicationRunner applyBucketPolicy(S3Client s3) {
        return args -> {
            String policy = new String(
                    bucketPolicy.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );
            s3.putBucketPolicy(PutBucketPolicyRequest.builder()
                    .bucket(props.bucket())
                    .policy(policy)
                    .build());
            log.info("Bucket policy applied to '{}'", props.bucket());
        };
    }
}