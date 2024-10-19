package com.sakhee.finman.expenso.dto;

public class LoginDto {

    private String username;
    @SuppressWarnings("unused")
	private String password;

    // Constructors, Getters, Setters
    public LoginDto() {}

    public LoginDto(String username, String password) {
        this.setUsername(username);
        this.password = password;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    // Getters and setters
}

