package com.taishow.dao;

import com.taishow.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeatStatusRepository extends JpaRepository<SeatStatus, Integer> {
}
