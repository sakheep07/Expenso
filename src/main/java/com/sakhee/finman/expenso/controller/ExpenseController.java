package com.sakhee.finman.expenso.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sakhee.finman.expenso.dto.ExpenseDto;
import com.sakhee.finman.expenso.entity.Expense;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.UserRepository;
import com.sakhee.finman.expenso.service.impl.ExpenseService;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getAllExpenses(Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username);
        model.addAttribute("expenses", expenseService.getExpensesByUser(user));
        return "expenses";
    }

    @GetMapping("/add")
    public String addExpenseForm(Model model) {
        model.addAttribute("expense", new ExpenseDto());
        model.addAttribute("expenseId", null);  // New expense mode
        return "add-expense";
    }

    @PostMapping("/save")
    public String saveExpense(@ModelAttribute("expense") ExpenseDto expenseDTO) {
        Expense expense = new Expense();
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(LocalDate.parse(expenseDTO.getDate()));
        expense.setNote(expenseDTO.getNote());
        expenseService.saveExpense(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/edit/{id}")
    public String editExpenseForm(@PathVariable Long id, Model model) {
        Expense expense = expenseService.getExpenseById(id);
        ExpenseDto expenseDto = new ExpenseDto();  
        expenseDto.setCategory(expense.getCategory());
        expenseDto.setAmount(expense.getAmount());
        expenseDto.setDate(expense.getDate().toString());
        expenseDto.setNote(expense.getNote());
        model.addAttribute("expense", expenseDto);
        model.addAttribute("expenseId", id);
        return "add-expense";  // Same form for adding and editing
    }

    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable Long id, @ModelAttribute("expense") ExpenseDto expenseDTO) {
        Expense expense = new Expense();
        expense.setId(id);  // Use existing expense ID to update
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(LocalDate.parse(expenseDTO.getDate()));
        expense.setNote(expenseDTO.getNote());
        expenseService.updateExpense(id, expense);
        return "redirect:/expenses";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }
}
