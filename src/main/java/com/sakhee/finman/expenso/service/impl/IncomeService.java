package com.sakhee.finman.expenso.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sakhee.finman.expenso.dto.IncomeDTO;
import com.sakhee.finman.expenso.entity.Income;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.IncomeRepository;
import com.sakhee.finman.expenso.repository.UserRepository;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

	public List<Income> getIncomeByUser(User user) {
		return incomeRepository.findByUser(user);
	}
    
    public IncomeDTO saveIncome(IncomeDTO incomeDTO) {
        // Get current logged-in user
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username);

        // Convert DTO to entity
        Income income = new Income();
        income.setSource(incomeDTO.getSource());
        income.setAmount(incomeDTO.getAmount());
        income.setDate(incomeDTO.getDate());
        income.setNote(incomeDTO.getNote());
        income.setUser(user);

        Income savedIncome = incomeRepository.save(income);

        // Convert entity back to DTO
        incomeDTO.setId(savedIncome.getId());
        return incomeDTO;
    }

    public List<IncomeDTO> getAllIncomesByUser(User user) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        user = userRepository.findByEmail(username);
        List<Income> incomes = incomeRepository.findByUser(user);
        return incomes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public IncomeDTO getIncomeById(Long id) {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid income ID"));
        return convertToDTO(income);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    private IncomeDTO convertToDTO(Income income) {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setId(income.getId());
        incomeDTO.setSource(income.getSource());
        incomeDTO.setAmount(income.getAmount());
        incomeDTO.setDate(income.getDate());
        incomeDTO.setNote(income.getNote());
        return incomeDTO;
    }

 // IncomeService
    public BigDecimal calculateTotalIncome(User user, LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Method to categorize income by source
    public Map<String, BigDecimal> categorizeIncomeBySource(User user, LocalDate startDate, LocalDate endDate) {
        List<Income> incomes = incomeRepository.findByUserAndDateBetween(user, startDate, endDate);
        Map<String, BigDecimal> incomeBySource = new HashMap<>();

        for (Income income : incomes) {
            incomeBySource.merge(income.getSource(), income.getAmount(), BigDecimal::add);
        }

        return incomeBySource;
    }
    
    public List<Income> getIncomeDetails(User user, LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findByUserAndDateBetween(user, startDate, endDate);
    }

}

