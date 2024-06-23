package com.taishow.dao;

import com.taishow.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SeatStatusRepository extends JpaRepository<SeatStatus, Integer> {

    @Query("SELECT p.payStatus " +
            "FROM SeatStatus ss " +
            "JOIN Tickets t ON ss.id = t.seatStatusId " +
            "JOIN Orders o ON t.ordersId = o.id " +
            "JOIN Payment p ON o.id = p.ordersId " +
            "WHERE ss.id = :id " +
            "ORDER BY p.modifyTime DESC")
    public List<String> getPayStatusById(Integer id);
}
