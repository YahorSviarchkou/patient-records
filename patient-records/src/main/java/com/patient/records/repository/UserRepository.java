package com.patient.records.repository;

import com.patient.records.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);

    List<User> findAllByDeletedOrderById(Boolean deleted);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password=:password WHERE id=:id", nativeQuery = true)
    void updateUser(@Param("password") String password, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET deleted=:deleted WHERE id=:id", nativeQuery = true)
    void setDeletedInUser(@Param("id") Long id, @Param("deleted") Boolean deleted);
}
