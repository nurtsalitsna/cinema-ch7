package com.spring.binar.challenge_5.models;

import com.spring.binar.challenge_5.dto.ScheduleRequestDTO;
import com.spring.binar.challenge_5.dto.ScheduleResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter @Getter
@NoArgsConstructor
@Table(name = "schedule", schema = "public")
@Entity
@AllArgsConstructor
@ToString
@Builder
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false, unique = true)
    private int scheduleId;

    @Column(name = "from_date", nullable = false)
    private long fromDate;

    @Column(name = "to_date", nullable = false)
    private long toDate;

    @Column(name = "price", nullable = false)
    private int price;

    @OneToOne(targetEntity = Studio.class,cascade = CascadeType.MERGE)
    @JoinColumn(name = "studio_id", referencedColumnName = "studio_id", nullable = false)
    private Studio studio;

    @OneToOne(targetEntity = Film.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "film_id", referencedColumnName = "film_id", nullable = false)
    private Film film;

    public ScheduleResponseDTO convertToResponse(List<Seat> seats){
        return ScheduleResponseDTO.builder()
                .film(this.film)
                .studio(this.studio)
                .fromDate(this.fromDate)
                .price(this.price)
                .scheduleId(this.scheduleId)
                .toDate(this.toDate)
                .availableSeats(seats)
                .build();
    }

}
