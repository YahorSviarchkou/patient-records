package com.patient.records.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "doctor_details")
@ToString(callSuper = true, exclude = {"comments"})
@EqualsAndHashCode(callSuper = true, exclude = {"comments"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorDetails extends AbstractEntity {

    String fio;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "position_id")
    Position position;

    @JsonIgnore
    @OneToMany(mappedBy = "doctorDetails")
    Set<Comment> comments = new HashSet<>();
}
