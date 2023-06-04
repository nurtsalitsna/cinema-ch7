package com.spring.binar.challenge_5.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.binar.challenge_5.dto.PaymentResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "payment", schema = "public")
@Entity
@Builder
@AllArgsConstructor
@ToString
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private int paymentId;

    @Column(name = "payment_date")
    private Long paymentDate;

    @Column(name = "amount")
    private int amount;

    @OneToOne(targetEntity = Schedule.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id")
    @ToString.Exclude
    private Schedule schedule;

    @OneToOne(targetEntity = Costumer.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "costumer_id", referencedColumnName = "costumer_id")
    @ToString.Exclude
    private Costumer costumer;

    @OneToOne(targetEntity = Staff.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @ToString.Exclude
    private Staff staff;
    public List<SeatReserved> toSeatReserved(List<Seat> seats){
        List<SeatReserved> seatsReserved= new ArrayList<>();
        for(Seat seat: seats){
            seatsReserved.add(SeatReserved.builder()
                            .seat(seat)
                            .payment(this)
                            .schedule(this.getSchedule())
                            .build());
        }
        return seatsReserved;
    }

    public PaymentResponseDTO toPaymentResponseDTO(List<Seat> seats){
        return PaymentResponseDTO.builder()
                .seatsReserved(seats)
                .paymentId(this.paymentId)
                .amount(this.amount)
                .moneyChange(this.amount - this.getSchedule().getPrice())
                .staff(this.staff.convertToStaffDto())
                .paymentDate(new Date(this.paymentDate))
                .costumer(this.costumer.convertToCostumerDto())
                .schedule(this.schedule)
                .build();
    }

    public Invoice toInvoice(){
        return Invoice.builder()
        .costumerId(this.costumer.getCostumerId())
        .paymentId(this.paymentId)
        .paymentDate(new Date(this.paymentDate))
        .amount(amount)
        .fromDate(this.schedule.getFromDate())
        .toDate(this.schedule.getToDate())
        .title(this.schedule.getFilm().getTitle())
        .studioName(this.schedule.getStudio().getName())
        .username(this.costumer.getFirstName())
        .build();
    }
}
