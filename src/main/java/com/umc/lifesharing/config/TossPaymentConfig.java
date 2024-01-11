package com.umc.lifesharing.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TossPaymentConfig {
    @Value("${payment.toss.test_client_api_key}")
    private String testClientApiKey;

    @Value("${payment.toss.test_secrete_api_key}")
    private String testSecretKey;

    /* @Value("${payment.toss.live_client_api_key}")
    private String liveClientApiKey;
    @Value("${payment.toss.live_secrete_api_key}")
    private String LiveSeceretKey ;*/

    @Value("${payment. toss.success_url}")
    private String successUrl;

    @Value("${payment. toss. fail url}")
    private String failUrl;

    public static final String PAYMENT_URL = "https://api.tosspayments.com/v1/payments/";


}
