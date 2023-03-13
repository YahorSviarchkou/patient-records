package com.patient.records.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comments")
@ToString(callSuper = true, exclude = {"patient"})
@EqualsAndHashCode(callSuper = true, exclude = {"patient"})
public class Comment extends AbstractEntity {

    String content;

    @Column(updatable = false)
    @CreationTimestamp
    Timestamp created;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    DoctorDetails doctorDetails;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_id")
    PatientCard patient;
}
