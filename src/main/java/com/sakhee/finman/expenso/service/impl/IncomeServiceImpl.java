package com.sakhee.finman.expenso.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sakhee.finman.expenso.dto.IncomeDTO;
import com.sakhee.finman.expenso.entity.Income;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.IncomeRepository;
import com.sakhee.finman.expenso.repository.UserRepository;
import com.sakhee.finman.expenso.service.IncomeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeServiceImpl(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

	@Override
	public List<Income> getIncomeByUser(User user) {
		return incomeRepository.findByUser(user);
	}
    
    @Override
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

    @Override
    public List<IncomeDTO> getAllIncomesByUser(User user) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        user = userRepository.findByEmail(username);
        List<Income> incomes = incomeRepository.findByUser(user);
        return incomes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public IncomeDTO getIncomeById(Long id) {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid income ID"));
        return convertToDTO(income);
    }

    @Override
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


}

