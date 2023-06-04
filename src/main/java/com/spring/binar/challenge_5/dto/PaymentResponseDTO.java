package com.spring.binar.challenge_5.dto;

import com.spring.binar.challenge_5.models.Costumer;
import com.spring.binar.challenge_5.models.Schedule;
import com.spring.binar.challenge_5.models.Seat;
import com.spring.binar.challenge_5.models.Staff;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PaymentResponseDTO {
    private int paymentId;
    private Date paymentDate;
    private int amount;
    private int moneyChange;
    private Schedule schedule;
    private CostumerResponseDto costumer;
    private StaffResponseDto staff;
    private List<Seat> seatsReserved;
}


