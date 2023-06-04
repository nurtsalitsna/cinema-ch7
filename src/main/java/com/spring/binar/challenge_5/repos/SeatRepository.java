package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface SeatRepository extends JpaRepository <Seat, Integer>{
//    @Query(value = "SELECT s.seat_id, s.row, s.number FROM Seat s LEFT JOIN SeatReserved sv ON s.seat_id = sv.seat_id where sv.schedule_id = ?1", nativeQuery = true)
//    List<Seat> findSeatAvailable( Integer scheduleId);

//    @Query("SELECT * FROM Seat WHERE studioId = ?1")
//    List<Seat> findAllByStudioStudioId(Integer studioId);

//    @Transactional
//    @Modifying
//    @Query(value = "SELECT s.seat_id, s.row, s.number FROM Seat s LEFT JOIN SeatReserved sv ON s.seat_id = sv.seat_id where sv.paymentId = :paymentId")
//    List<Seat> findSeatsByPaymentId(@Param("paymentId") Integer paymentId);

    @Query(value = "select NEW com.spring.binar.challenge_5.models.Seat(s.seatId, s.row, s.number) from Seat s " +
            "where s.seatId NOT IN (SELECT seat.seatId from SeatReserved where schedule.scheduleId = ?1) and s.studio.studioId = ?2")
    List<Seat> findAvailableSeats(Integer scheduleId, Integer studioId);

    List<Seat> findByStudioStudioId(Integer studioId);

//    boolean existByStudioStudioId(Integer studioId);
}
