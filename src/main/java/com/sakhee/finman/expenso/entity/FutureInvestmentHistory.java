package com.sakhee.finman.expenso.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "future_investment_history")
public class FutureInvestmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double monthlyInvestment;
    private int investmentPeriod;
    private String riskLevel;
    private double expectedRate;
    private double futureValue;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    

    // Getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getMonthlyInvestment() {
		return monthlyInvestment;
	}

	public void setMonthlyInvestment(double monthlyInvestment) {
		this.monthlyInvestment = monthlyInvestment;
	}

	public int getInvestmentPeriod() {
		return investmentPeriod;
	}

	public void setInvestmentPeriod(int investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public double getExpectedRate() {
		return expectedRate;
	}

	public void setExpectedRate(double expectedRate) {
		this.expectedRate = expectedRate;
	}

	public double getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(double futureValue) {
		this.futureValue = futureValue;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    
}

