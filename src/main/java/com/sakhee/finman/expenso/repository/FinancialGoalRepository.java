package com.sakhee.finman.expenso.repository;

import com.sakhee.finman.expenso.entity.FinancialGoal;
import com.sakhee.finman.expenso.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    List<FinancialGoal> findByUserAndIsCompletedFalse(User user);
    List<FinancialGoal> findByUserAndIsCompletedTrue(User user); // For goal history
}
