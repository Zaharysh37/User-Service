package com.innowise.userservice.core.dao;

import com.innowise.userservice.core.entity.CardInfo;
import com.innowise.userservice.core.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CardInfo c WHERE c.expirationDate = :expiredDateString")
    int deleteAllExpiredCards(@Param("expiredDateString") String expiredDateString);

    @Query("SELECT c.user FROM CardInfo c WHERE c.number = :cardNumber")
    Optional<User> findUserByCardNumber(@Param("cardNumber") String cardNumber);
}
