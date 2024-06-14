package com.taishow.dao;

import com.taishow.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Tickets, Integer> {
}
