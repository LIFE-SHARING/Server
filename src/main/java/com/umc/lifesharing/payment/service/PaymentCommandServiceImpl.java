package com.umc.lifesharing.payment.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.PaymentHandler;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.payment.entity.TossPayment;
import com.umc.lifesharing.payment.repository.TossPaymentRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final TossPaymentRepository tossPaymentRepository;
    private final UserRepository userRepository;

    @Override
    public TossPayment requestTossPayment(TossPayment payment, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (payment.getAmount() < 1000) {
            throw new PaymentHandler(ErrorStatus.INVALID_PAYMENT_AMOUNT);
        }
        payment.setUser(user);
        return tossPaymentRepository.save(payment);
    }
}
