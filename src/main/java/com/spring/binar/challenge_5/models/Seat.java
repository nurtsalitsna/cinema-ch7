package com.spring.binar.challenge_5.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "seat",schema = "public")
public class Seat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // use identity when data type in db is serial
    @Column(name = "seat_id", nullable = false, unique = true)
    private int seatId;

    @Column(name = "row")
    private char row;

    @Column(name = "number")
    private byte number;

    public Seat(int seatId, char row, byte number) {
        this.seatId = seatId;
        this.row = row;
        this.number = number;
    }

    @JsonBackReference
    @OneToOne(targetEntity = Studio.class,cascade = CascadeType.MERGE)
    @JoinColumn(name = "studio_id", referencedColumnName = "studio_id", nullable = false)
    private Studio studio;

//    @JsonBackReference
//    @ManyToOne(targetEntity = SeatReserved.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private SeatReserved seatReserved;
}
