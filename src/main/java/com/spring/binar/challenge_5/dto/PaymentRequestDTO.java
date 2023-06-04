package com.spring.binar.challenge_5.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PaymentRequestDTO {
    int paymentId;
    int amount;
    int scheduleId;
    int costumerId;
    int staffId;
    List<Integer> seatIds;

//    public Payment toPayment(Payment payment){
//        return payment.builder()
//                .
//    }
}
