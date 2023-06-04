package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
}
