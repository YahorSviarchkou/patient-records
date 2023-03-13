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
@Table(name = "users")
@ToString(callSuper = true, exclude = {"password", "doctorDetails"})
@EqualsAndHashCode(callSuper = true, exclude = {"doctorDetails"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractEntity {

    String login;
    Boolean deleted;

    @JsonIgnore
    String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    DoctorDetails doctorDetails;
}
