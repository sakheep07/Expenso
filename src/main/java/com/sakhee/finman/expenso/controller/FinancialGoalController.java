package com.sakhee.finman.expenso.controller;

import com.sakhee.finman.expenso.dto.FinancialGoalDTO;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.UserRepository;
import com.sakhee.finman.expenso.service.impl.FinancialGoalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/goals")
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService financialGoalService;

    @Autowired
    private UserRepository userRepository;

    // Display active financial goals
    @GetMapping
    public String viewGoals(Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username);
        List<FinancialGoalDTO> goals = financialGoalService.getUserGoals(user);
        model.addAttribute("goals", goals);
        model.addAttribute("goalDTO", new FinancialGoalDTO());
        return "goals";
    }

    // Save a new goal
    @PostMapping("/add")
    public String saveGoal(@ModelAttribute FinancialGoalDTO goalDTO) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username);
        financialGoalService.saveGoal(goalDTO, user);
        return "redirect:/goals";
    }

    // Update goal progress
    @PostMapping("/update/{id}")
    public String updateGoal(@PathVariable Long id, @RequestParam BigDecimal currentAmount) {
        financialGoalService.updateGoalAmount(id, currentAmount);
        return "redirect:/goals";
    }

    // Display goal history (archived)
    @GetMapping("/history")
    public String viewGoalHistory(Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username);
        List<FinancialGoalDTO> goalHistory = financialGoalService.getGoalHistory(user);
        model.addAttribute("goalHistory", goalHistory);
        return "goals";
    }
}
