package com.patient.records.repository;

import com.patient.records.entity.PatientCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PatientCardRepository extends JpaRepository<PatientCard, Long> {

    List<PatientCard> findAllByDeletedOrderById(Boolean deleted);

    @Modifying
    @Transactional
    @Query(value = "UPDATE patient_cards SET photo=:photo WHERE id=:id", nativeQuery = true)
    void setPhoto(@Param("photo") byte[] photo, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE patient_cards SET deleted=:deleted WHERE id=:id", nativeQuery = true)
    void setDeletedInPatientCard(@Param("id") Long id, @Param("deleted") Boolean deleted);
}
