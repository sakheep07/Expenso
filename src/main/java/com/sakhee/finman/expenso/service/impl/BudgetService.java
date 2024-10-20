package com.sakhee.finman.expenso.service.impl;

import com.sakhee.finman.expenso.dto.BudgetDTO;
import com.sakhee.finman.expenso.entity.Budget;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.BudgetRepository;
import com.sakhee.finman.expenso.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BudgetDTO> getUserBudgets(User user) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        user = userRepository.findByEmail(username);
        List<Budget> budgets = budgetRepository.findByUser(user);
        return budgets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public BudgetDTO saveBudget(BudgetDTO budgetDTO) {
    	
    	 String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
         User user = userRepository.findByEmail(username);
    	
        Budget budget = new Budget();
        budget.setCategory(budgetDTO.getCategory());
        budget.setAllocatedAmount(budgetDTO.getAllocatedAmount());
        budget.setSpentAmount(budgetDTO.getSpentAmount());
        
        // Optionally set the current user
         budget.setUser(user); // If you want to associate with the logged-in user

         Budget savedBudget = budgetRepository.save(budget);
         
         budgetDTO.setId(savedBudget.getId());
         return budgetDTO;
    }

    // Convert between entity and DTO
    private BudgetDTO convertToDTO(Budget budget) {
        BudgetDTO dto = new BudgetDTO();
        dto.setId(budget.getId());
        dto.setCategory(budget.getCategory());
        dto.setAllocatedAmount(budget.getAllocatedAmount());
        dto.setSpentAmount(budget.getSpentAmount());
        return dto;
    }

    private Budget convertToEntity(BudgetDTO budgetDTO) {
        Budget budget = new Budget();
        budget.setId(budgetDTO.getId());
        budget.setCategory(budgetDTO.getCategory());
        budget.setAllocatedAmount(budgetDTO.getAllocatedAmount());
        budget.setSpentAmount(budgetDTO.getSpentAmount());
        return budget;
    }
}
