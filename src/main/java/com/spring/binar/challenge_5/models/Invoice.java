package com.spring.binar.challenge_5.models;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Invoice {
    private int costumerId;
    private int paymentId;
    private Date paymentDate;
    private int amount;
    private Long fromDate;
    private Long toDate;
    private String title;
    private String studioName;
    // private char row;
    // private byte number;
    private String username;
    
}
