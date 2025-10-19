package com.innowise.userservice.core.dao;

import com.innowise.userservice.core.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
}
