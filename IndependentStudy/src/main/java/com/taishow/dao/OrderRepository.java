package com.taishow.dao;

import com.taishow.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    public Optional<Orders> findByOrderNum(String orderNum);
}
