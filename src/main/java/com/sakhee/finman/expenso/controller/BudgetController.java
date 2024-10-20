package com.sakhee.finman.expenso.controller;

import com.sakhee.finman.expenso.dto.BudgetDTO;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.UserRepository;
import com.sakhee.finman.expenso.service.impl.BudgetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String viewBudgets(Model model) {
    	
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username); // Ensure this returns a valid user
        
        List<BudgetDTO> budgets = budgetService.getUserBudgets(user);
        model.addAttribute("budgets", budgets);
        model.addAttribute("budgetDTO", new BudgetDTO()); // for the form
        return "budget"; // Ensure this matches the Thymeleaf template name
    }

    @PostMapping("/save")
    public String saveBudget(@ModelAttribute BudgetDTO budgetDTO) {
        budgetService.saveBudget(budgetDTO);
        return "redirect:/budget";
    }
}
