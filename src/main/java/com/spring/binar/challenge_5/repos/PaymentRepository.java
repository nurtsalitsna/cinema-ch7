package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
//    @Query(value = "select p.payment_id, p.costumer_id, p.schedule_id, p.payment_date, p.amount ," +
//            "s.studio_id, s.number, s.row from payment p " +
//            "join seat_reserved sr on p.payment_id = sr.payment_id join seat s on sr.seat_id = s.seat_id " +
//            "where sr.schedule_id = ?1", nativeQuery = true)
//    List<Payment> findByScheduleScheduleId(Integer scheduledId);
}
