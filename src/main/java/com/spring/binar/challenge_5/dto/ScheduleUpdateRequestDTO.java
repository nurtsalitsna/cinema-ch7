package com.spring.binar.challenge_5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleUpdateRequestDTO {
    private Integer scheduleId;
    private long fromDate;
    private long toDate;
    private int price;
    private int studioId;
    private int filmId;
}
