package com.innowise.userservice.core.dao;

import com.innowise.userservice.core.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.name = :newName WHERE u.id = :id")
    int updateUserName(@Param("id") Long id, @Param("newName") String newName);

    @Query(value =
        "SELECT * FROM users u " +
        "WHERE u.surname LIKE :letter || '%'",
        nativeQuery = true)
    Page<User> findBySurnameStartsWith(@Param("letter") String letter, Pageable pageable);
}
