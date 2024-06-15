package com.taishow.dao;

import com.taishow.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    public Optional<Payment> findByOrdersId(Integer ordersId);
}
