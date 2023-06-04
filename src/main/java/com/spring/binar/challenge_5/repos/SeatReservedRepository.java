package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.Seat;
import com.spring.binar.challenge_5.models.SeatReserved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SeatReservedRepository extends JpaRepository<SeatReserved, Integer> {

//    @Query("SELECT s.seat_id, s.row, s.number FROM Seat s LEFT JOIN SeatReserved sv ON s.seat_id = sv.seat_id where sv.paymentId = ?1")
//    List<Seat> findSeatsByPaymentId(Integer paymentId);
    


    List<SeatReserved> findSeatsByPaymentPaymentId(Integer paymentId);

    @Modifying
    @Transactional
    @Query(value = "delete from SeatReserved s where s.payment.paymentId = ?1")
    void removeAllByPaymentPaymentId(Integer paymentId);


}
