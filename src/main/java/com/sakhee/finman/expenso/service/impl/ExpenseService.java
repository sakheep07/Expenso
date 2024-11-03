package com.sakhee.finman.expenso.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sakhee.finman.expenso.entity.Expense;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.ExpenseRepository;
import com.sakhee.finman.expenso.repository.UserRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private UserRepository userRepository; //  UserRepository to fetch user details


    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }
    
    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user); // Assuming you have this method in ExpenseRepository
    }

    public Expense saveExpense(Expense expense) {
        // Get the current logged-in user
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username); // Assuming email is used as username

        expense.setUser(user); // Set the user for the expense
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expense.setCategory(updatedExpense.getCategory());
            expense.setAmount(updatedExpense.getAmount());
            expense.setDate(updatedExpense.getDate());
            expense.setNote(updatedExpense.getNote());
            return expenseRepository.save(expense);
        }
        return null;
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
    
    // Calculate the total expenses for a user within a date range
    public BigDecimal calculateTotalExpenses(User user, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream()
                .map(Expense::getAmount) // Ensure this returns BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Correctly using BigDecimal for reduction
    }
    
 // This method queries the database to sum expenses by category for the user within the date range.
    public Map<String, BigDecimal> calculateExpensesByCategory(User user, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate);
        
        return expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
            ));
    }
}

