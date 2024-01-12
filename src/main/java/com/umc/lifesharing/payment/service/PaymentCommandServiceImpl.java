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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

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
        payment.setSucceed(true);
        payment.getUser().setPoint(payment.getUser().getPoint() + amount);
        userRepository.updateUserById(payment.getUser(), payment.getUser().getId());
        return result;
    }
    public TossPayment verifyPayment(String orderId, Long amount) {
        TossPayment payment = tossPaymentRepository.findByOrderId(orderId).
                orElseThrow(() -> new PaymentHandler(ErrorStatus.PAYMENT_NOT_FOUND));
        if (!payment.getAmount().equals(amount)) {
            throw new PaymentHandler(ErrorStatus.PAYMENT_NOT_FOUND);
        }
        return payment;
    }
    @Transactional
    @Override
    public TossPaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();//키/값 쌍을 문자열이 아닌 오브젝트로 보낼 수 있음
        params.put("orderId", orderId);
        params.put("amount", amount);

        TossPaymentSuccessDto result = null;
        try { //post요청 (url , HTTP객체 ,응답 Dto)
            result = restTemplate.postForObject(TossPaymentConfig.PAYMENT_URL + paymentKey,
                    new HttpEntity<>(params, headers),
                    TossPaymentSuccessDto.class);
        } catch (Exception e) {
            throw new PaymentHandler(ErrorStatus.ALREADY_APPROVED);
        }
        return result;
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
