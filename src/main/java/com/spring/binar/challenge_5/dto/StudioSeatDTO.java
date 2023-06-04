package com.spring.binar.challenge_5.dto;

import com.spring.binar.challenge_5.models.Seat;
import lombok.Data;

import java.util.List;

@Data
public class StudioSeatDTO {
    private int studioId;
    private int capacity;
    private String name;
    private List<Seat> seatsAvailable;
}
