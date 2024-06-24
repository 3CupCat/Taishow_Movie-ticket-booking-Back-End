package com.taishow.dao;

import com.taishow.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonusDao extends JpaRepository<Bonus, Integer> {

    @Query("SELECT o, p, t, s, m, sc, th, b FROM Orders o " +
            "LEFT JOIN Payment p ON o.id = p.ordersId " +
            "LEFT JOIN Tickets t ON o.id = t.ordersId " +
            "LEFT JOIN ShowTime s ON t.showTimeId = s.id " +
            "LEFT JOIN Movie m ON s.movieId = m.id " +
            "LEFT JOIN Screen sc ON s.screenId = sc.id " +
            "LEFT JOIN Theaters th ON sc.theaterId = th.id " +
            "LEFT JOIN Bonus b ON p.id = b.paymentId " +
            "WHERE o.userId = :userId")
    List<Object[]> findByAll(@Param("userId") Integer userId);
}
