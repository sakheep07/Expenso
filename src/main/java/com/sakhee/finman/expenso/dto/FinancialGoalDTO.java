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
    // Method to calculate the progress percentage
    public double getProgressPercentage() {
        if (currentAmount == null || targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        return currentAmount.divide(targetAmount, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).doubleValue();
    }
    

    // Calculate milestone progress
    // Add the milestoneProgress method
    public String milestoneProgress() {
        if (currentAmount == null || targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return "No progress yet";
        }
        
        BigDecimal progressPercentage = currentAmount.divide(targetAmount, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

        if (progressPercentage.compareTo(new BigDecimal(25)) >= 0 && progressPercentage.compareTo(new BigDecimal(50)) < 0) {
            return "25% Complete";
        } else if (progressPercentage.compareTo(new BigDecimal(50)) >= 0 && progressPercentage.compareTo(new BigDecimal(75)) < 0) {
            return "50% Complete";
        } else if (progressPercentage.compareTo(new BigDecimal(75)) >= 0 && progressPercentage.compareTo(new BigDecimal(100)) < 0) {
            return "75% Complete";
        } else if (progressPercentage.compareTo(new BigDecimal(100)) >= 0) {
            return "Goal Completed!";
        } else {
            return progressPercentage.intValue() + "% Complete";
        }
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
