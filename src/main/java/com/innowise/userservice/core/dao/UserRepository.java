package com.innowise.userservice.core.dao;

import com.innowise.userservice.core.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"cards"})
    Optional<User> findById(Long id);

    @Query(value =
        "SELECT * FROM users u " +
        "WHERE u.surname LIKE :letter || '%'",
        nativeQuery = true)
    Page<User> findBySurnameStartsWith(@Param("letter") String letter, Pageable pageable);

    Optional<User> findBySub(UUID sub);

    boolean existsBySub(UUID sub);
}
