package com.sakhee.finman.expenso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sakhee.finman.expenso.entity.Expense;
import com.sakhee.finman.expenso.entity.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByUser(User user);
}



