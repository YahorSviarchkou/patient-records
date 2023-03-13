package com.patient.records.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "positions")
@ToString(callSuper = true, exclude = "doctorDetails")
@EqualsAndHashCode(callSuper = true, exclude = "doctorDetails")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Position extends AbstractEntity {

    String title;

    @JsonIgnore
    @OneToMany(mappedBy = "position")
    Set<DoctorDetails> doctorDetails = new LinkedHashSet<>();
}
