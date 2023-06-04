package com.spring.binar.challenge_5.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "seat_reserved", schema = "public")
@Entity
@Builder
@ToString
public class SeatReserved implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = Seat.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "seat_id", referencedColumnName ="seat_id")
    private Seat seat;

    @JoinColumn(name = "payment_id",referencedColumnName = "payment_id",nullable = false)
    @OneToOne(targetEntity = Payment.class, cascade = CascadeType.MERGE)
    private Payment payment;

    @JoinColumn(name = "schedule_id",referencedColumnName = "schedule_id",nullable = false)
    @OneToOne(targetEntity = Schedule.class, cascade = CascadeType.MERGE)
    private Schedule schedule;


}
