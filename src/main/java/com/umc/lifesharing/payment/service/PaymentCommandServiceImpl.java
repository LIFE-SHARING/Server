package com.umc.lifesharing.payment.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.PaymentHandler;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.TossPaymentConfig;
import com.umc.lifesharing.payment.dto.TossPaymentSuccessDto;
import com.umc.lifesharing.payment.entity.TossPayment;
import com.umc.lifesharing.payment.repository.TossPaymentRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.umc.lifesharing.config.TossPaymentConfig.PAYMENT_URL;

@Service
@RequiredArgsConstructor
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final TossPaymentRepository tossPaymentRepository;
    private final UserRepository userRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final UserQueryService userQueryService;

    @Override // 결제 요청
    public TossPayment requestTossPayment(TossPayment payment, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (payment.getAmount() < 1000) {
            throw new PaymentHandler(ErrorStatus.INVALID_PAYMENT_AMOUNT);
        }
        payment.setUser(user);
        return tossPaymentRepository.save(payment);
    }

    @Transactional // 결제 요청 성공시
    @Override
    public TossPaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        TossPayment payment = verifyPayment(orderId, amount);
        TossPaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        payment.setPaymentKey(paymentKey);//추후 결제 취소 / 결제 조회
        payment.setIsSucceed(true);
        //payment.getUser().setPoint(payment.getUser().getPoint() + amount);
        payment.getUser().updateAddPoint(amount); // 포인트 추가
        //userRepository.updateUserById(payment.getUser(), payment.getUser().getId());
        return result;
    }
    public TossPayment verifyPayment(String orderId, Long amount) {
        TossPayment payment = tossPaymentRepository.findByOrderId(orderId).
                orElseThrow(() -> new PaymentHandler(ErrorStatus.ORDER_ID_NOT_FOUND));
        if (!payment.getAmount().equals(amount)) {
            System.out.println("amount : " + amount);
            System.out.println("payment.getAmount() : " + payment.getAmount());
            throw new PaymentHandler(ErrorStatus.PAYMENT_NOT_FOUND);
        }
        return payment;
    }
    @Transactional
    @Override
    public TossPaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        String testSecretApiKey = tossPaymentConfig.getTestSecretKey() + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));

        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        param.put("paymentKey", paymentKey);
        param.put("orderId", orderId);
        param.put("amount", amount.longValue());

        return rest.postForEntity(
                PAYMENT_URL + paymentKey,
                new HttpEntity<>(param, headers),
                TossPaymentSuccessDto.class
        ).getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder().encode((tossPaymentConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
