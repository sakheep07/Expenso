package com.sakhee.finman.expenso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sakhee.finman.expenso.entity.LoanEmiHistory;
import com.sakhee.finman.expenso.entity.User;

@Repository
public interface LoanEmiHistoryRepository extends JpaRepository<LoanEmiHistory, Long> {
    List<LoanEmiHistory> findByUser(User user);
}

