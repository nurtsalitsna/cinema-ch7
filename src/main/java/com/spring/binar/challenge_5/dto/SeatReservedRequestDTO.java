package com.spring.binar.challenge_5.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class SeatReservedRequestDTO implements Serializable {
    private int id;
    private List<Integer> seatIds;
    private int paymentId;
    private int scheduleId;
}
