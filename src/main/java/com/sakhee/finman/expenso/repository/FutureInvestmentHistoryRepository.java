package com.sakhee.finman.expenso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sakhee.finman.expenso.entity.FutureInvestmentHistory;
import com.sakhee.finman.expenso.entity.User;

@Repository
public interface FutureInvestmentHistoryRepository extends JpaRepository<FutureInvestmentHistory, Long> {
	 List<FutureInvestmentHistory> findByUser(User user);
}

