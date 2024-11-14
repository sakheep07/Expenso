package com.sakhee.finman.expenso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sakhee.finman.expenso.dto.IncomeDTO;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.UserRepository;
import com.sakhee.finman.expenso.service.impl.IncomeService;

import java.util.List;

@Controller
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private UserRepository userRepository;

    // Get all incomes for the user
    @GetMapping
    public String getAllIncomes(Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username); // Ensure this returns a valid user
        
        List<IncomeDTO> incomes = incomeService.getAllIncomesByUser(user); // Fetch incomes for the specific user
        model.addAttribute("incomes", incomes);
        model.addAttribute("incomeDTO", new IncomeDTO()); // For the add/edit form
        return "income"; // Thymeleaf page for listing and managing incomes
    }

    // Get the form for adding new income
    @GetMapping("/add")
    public String showAddIncomeForm(Model model) {
        model.addAttribute("incomeDTO", new IncomeDTO());
        return "income"; // Use the same income page for adding
    }

    // Handle the addition of new income
    @PostMapping("/add")
    public String addIncome(@ModelAttribute IncomeDTO incomeDTO) {
        incomeService.saveIncome(incomeDTO);
        return "redirect:/income"; // Redirect to income list after adding
    }
    
    // Handle the editing of income
    @PostMapping("/edit/{id}")
    public String editIncome(@PathVariable Long id, @ModelAttribute IncomeDTO incomeDTO) {
        incomeDTO.setId(id);
        incomeService.saveIncome(incomeDTO);
        return "redirect:/income"; // Redirect to income list after editing
    }

    @GetMapping("/edit/{id}")
    public String showEditIncomeForm(@PathVariable Long id, Model model) {
        IncomeDTO incomeDTO = incomeService.getIncomeById(id);
        model.addAttribute("incomeDTO", incomeDTO);
        return "income"; // Use the same page for adding/editing
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return "redirect:/income"; // Redirect to income list after deletion
    }

}
