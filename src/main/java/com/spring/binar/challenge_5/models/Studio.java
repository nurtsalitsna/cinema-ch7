package com.spring.binar.challenge_5.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
@Table(name = "studio", schema = "public")
public class Studio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studio_id", nullable = false, unique = true)
    private int studioId;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "name")
    private String name;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "studio")
//    private List<Seat> seats;
}
