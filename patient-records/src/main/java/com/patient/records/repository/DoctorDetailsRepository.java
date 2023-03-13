package com.patient.records.repository;

import com.patient.records.entity.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorDetailsRepository extends JpaRepository<DoctorDetails, Long> {

    @Query(value = "SELECT * FROM doctor_details d WHERE d.user_id IN (SELECT u.id FROM users u WHERE deleted=:deleted ORDER BY d.id)",
            nativeQuery = true)
    List<DoctorDetails> findAllByDeleted(Boolean deleted);

    @Query(value = "SELECT * FROM doctor_details d WHERE d.user_id IN (SELECT u.id FROM users u WHERE u.id=:id)",
            nativeQuery = true)
    Optional<DoctorDetails> findByUserId(Long id);
}
