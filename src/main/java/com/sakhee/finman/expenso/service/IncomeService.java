package com.sakhee.finman.expenso.service;

import java.util.List;

import com.sakhee.finman.expenso.dto.IncomeDTO;
import com.sakhee.finman.expenso.entity.Income;
import com.sakhee.finman.expenso.entity.User;

public interface IncomeService {

    IncomeDTO saveIncome(IncomeDTO incomeDTO);

    List<IncomeDTO> getAllIncomesByUser(User user);

    IncomeDTO getIncomeById(Long id);

    void deleteIncome(Long id);
    
    List<Income> getIncomeByUser(User user);
}

