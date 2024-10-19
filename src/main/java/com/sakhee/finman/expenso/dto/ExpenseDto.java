package com.sakhee.finman.expenso.dto;

public class ExpenseDto {

    private Long id;
    private String category;
	private double amount;
    private String date;
    private String note;
    
    public ExpenseDto() {}
    
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}


    // Getters and Setters
}

