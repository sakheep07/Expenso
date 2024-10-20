package com.sakhee.finman.expenso.repository;

import com.sakhee.finman.expenso.entity.Budget;
import com.sakhee.finman.expenso.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);
}
