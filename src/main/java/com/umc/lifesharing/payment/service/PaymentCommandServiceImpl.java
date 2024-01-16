package com.umc.lifesharing.payment.service;

import com.google.gson.Gson;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.PaymentHandler;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.TossPaymentConfig;
import com.umc.lifesharing.payment.dto.TossPaymentDto;
import com.umc.lifesharing.payment.dto.TossPaymentSuccessDto;
import com.umc.lifesharing.payment.entity.TossPayment;
import com.umc.lifesharing.payment.repository.TossPaymentRepository;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.reservation.repository.ReservationRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final TossPaymentRepository tossPaymentRepository;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final UserQueryService userQueryService;

    @Override // 결제 요청
    public TossPayment requestTossPayment(TossPaymentDto tossPaymentDto, Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.INVALID_PRODUCT_ID));
        TossPayment payment = tossPaymentDto.toEntity();
        if (payment.getAmount() < 1000) {
            throw new PaymentHandler(ErrorStatus.CHARGE_POINT);
        }
        // 결제 내역 생성
        Reservation reservation = Reservation.builder()
                .user(user)
                .product(product)
                .amount(tossPaymentDto.getAmount())
                .deposit(tossPaymentDto.getDeposit())
                .start_date(tossPaymentDto.getStartDate())
                .end_date(tossPaymentDto.getEndDate())
                .total_time(tossPaymentDto.getTotalTime())
                .build();
        reservationRepository.save(reservation);

        payment.setUser(user);
        payment.setAmount(tossPaymentDto.getAmount() + tossPaymentDto.getDeposit());
        return tossPaymentRepository.save(payment);
    }

    @Transactional // 결제 요청 성공시
    @Override
    public TossPaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) throws IOException, ParseException {
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
    public TossPaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) throws IOException, ParseException {

        String secretKey = tossPaymentConfig.getTestSecretKey() + ":";

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        paymentKey = URLEncoder.encode(paymentKey, StandardCharsets.UTF_8);

        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        JSONObject obj = new JSONObject();
        obj.put("paymentKey", paymentKey);
        obj.put("orderId", orderId);
        obj.put("amount", amount.toString());

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        System.out.println(jsonObject);

        Gson gson = new Gson();

        return gson.fromJson(String.valueOf(jsonObject), TossPaymentSuccessDto.class);
    }

    @Transactional
    @Override
    public void tossPaymentFail(String code, String message, String orderId) {
        TossPayment payment = tossPaymentRepository.findByOrderId(orderId).orElseThrow(() -> {
            throw new PaymentHandler(ErrorStatus.PAYMENT_NOT_FOUND);
        });
        payment.setIsSucceed(false);
        payment.setFailReason(message);
    }

}
