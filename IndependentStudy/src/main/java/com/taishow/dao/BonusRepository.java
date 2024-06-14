package com.taishow.dao;

import com.taishow.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BonusRepository extends JpaRepository<Bonus, Integer> {
}
