package com.sakhee.finman.expenso.service.impl;

import com.sakhee.finman.expenso.dto.FinancialGoalDTO;
import com.sakhee.finman.expenso.entity.FinancialGoal;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.FinancialGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinancialGoalService {

    @Autowired
    private FinancialGoalRepository financialGoalRepository;

    // Get all active goals
    public List<FinancialGoalDTO> getUserGoals(User user) {
        List<FinancialGoal> goals = financialGoalRepository.findByUserAndIsCompletedFalse(user);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Save a new goal or update existing one
    public void saveGoal(FinancialGoalDTO goalDTO, User user) {
        FinancialGoal goal = convertToEntity(goalDTO);
        goal.setUser(user);
        financialGoalRepository.save(goal);
    }

    // Update goal progress
    public void updateGoalAmount(Long id, BigDecimal currentAmount) {
        Optional<FinancialGoal> optionalGoal = financialGoalRepository.findById(id);
        if (optionalGoal.isPresent()) {
            FinancialGoal goal = optionalGoal.get();
            goal.setCurrentAmount(currentAmount);

            // Check if goal is completed
            if (currentAmount.compareTo(goal.getTargetAmount()) >= 0) {
                goal.setCompleted(true);
                goal.setCompletionDate(LocalDate.now());
            }

            financialGoalRepository.save(goal);
        }
    }

    // Archive completed goals (goal history)
    public List<FinancialGoalDTO> getGoalHistory(User user) {
        List<FinancialGoal> completedGoals = financialGoalRepository.findByUserAndIsCompletedTrue(user);
        return completedGoals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Helper methods to convert between DTO and Entity
    private FinancialGoalDTO convertToDTO(FinancialGoal goal) {
        FinancialGoalDTO dto = new FinancialGoalDTO();
        dto.setId(goal.getId());
        dto.setName(goal.getName());
        dto.setTargetAmount(goal.getTargetAmount());
        dto.setCurrentAmount(goal.getCurrentAmount());
        dto.setDueDate(goal.getDueDate());
        dto.setCompleted(goal.isCompleted());
        dto.setCompletionDate(goal.getCompletionDate());
        return dto;
    }

    private FinancialGoal convertToEntity(FinancialGoalDTO dto) {
        FinancialGoal goal = new FinancialGoal();
        goal.setId(dto.getId());
        goal.setName(dto.getName());
        goal.setTargetAmount(dto.getTargetAmount());
        goal.setCurrentAmount(dto.getCurrentAmount());
        goal.setDueDate(dto.getDueDate());
        goal.setCompleted(dto.isCompleted());
        goal.setCompletionDate(dto.getCompletionDate());
        return goal;
    }
}
