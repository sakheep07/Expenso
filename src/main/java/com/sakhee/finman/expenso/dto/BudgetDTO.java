package com.sakhee.finman.expenso.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {

    private Long id;
    private String category;
	private BigDecimal allocatedAmount;
    private BigDecimal spentAmount;
    
    public BudgetDTO(Long id2, String category2, BigDecimal allocatedAmount2, BigDecimal spentAmount2) {
		// TODO Auto-generated constructor stub
	}
	public BudgetDTO() {
		// TODO Auto-generated constructor stub
	}
	// Getters and Setters
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigDecimal getAllocatedAmount() {
		return allocatedAmount;
	}
	public void setAllocatedAmount(BigDecimal allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}
	public BigDecimal getSpentAmount() {
		return spentAmount;
	}
	public void setSpentAmount(BigDecimal spentAmount) {
		this.spentAmount = spentAmount;
	}


  
}
