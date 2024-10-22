package com.sakhee.finman.expenso.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class FinancialGoalDTO {

    private Long id;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate dueDate;
    private boolean isCompleted;
    private LocalDate completionDate;
    
    @SuppressWarnings("unused")
	private int progressPercentage; // Add this field

    public void setProgressPercentage(int progressPercentage) {
		this.progressPercentage = progressPercentage;
	}

	// Add a getter for progress percentage
    public int getProgressPercentage() {
        if (targetAmount != null && currentAmount != null && targetAmount.compareTo(BigDecimal.ZERO) > 0) {
            return currentAmount.multiply(BigDecimal.valueOf(100)).divide(targetAmount, 0, RoundingMode.DOWN).intValue();
        }
        return 0;
    }
    
   // Getters and Setters
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getTargetAmount() {
		return targetAmount;
	}
	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}
	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public LocalDate getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(LocalDate completionDate) {
		this.completionDate = completionDate;
	}

}
