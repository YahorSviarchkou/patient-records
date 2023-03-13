package com.patient.records.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "patient_cards")
@ToString(callSuper = true, exclude = "comments")
@EqualsAndHashCode(callSuper = true, exclude = "comments")
public class PatientCard extends AbstractEntity {

    byte[] photo;
    String fio;
    Timestamp dateOfBirthday;
    Boolean deleted;

    @Column(updatable = false)
    @CreationTimestamp
    Timestamp created;

    @JsonIgnore
    @OneToMany(mappedBy = "patient")
    Set<Comment> comments = new HashSet<>();
}
