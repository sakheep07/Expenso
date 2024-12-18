package com.sakhee.finman.expenso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sakhee.finman.expenso.entity.Income;
import com.sakhee.finman.expenso.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);
    List<Income> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}

