package com.patient.records.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
@ToString(callSuper = true, exclude = "users")
@EqualsAndHashCode(callSuper = true, exclude = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    PreparedRoles title;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    Set<User> users = new LinkedHashSet<>();

    public enum PreparedRoles {
        CHIEF_PHYSICIAN,
        PHYSICIAN,
        LOCAL_ADMIN,
        ADMIN;
    }
}
