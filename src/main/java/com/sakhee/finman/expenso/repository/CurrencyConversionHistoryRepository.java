package com.sakhee.finman.expenso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sakhee.finman.expenso.entity.CurrencyConversionHistory;

public interface CurrencyConversionHistoryRepository extends JpaRepository<CurrencyConversionHistory, Long> {
}

