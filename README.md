"# Expenso" 

Database Details :

CREATE DATABASE expenso;
USE expenso;

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users_roles (
    USER_ID BIGINT NOT NULL,
    ROLE_ID BIGINT NOT NULL,
    PRIMARY KEY (USER_ID, ROLE_ID),
    FOREIGN KEY (USER_ID) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ROLE_ID) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255) NOT NULL,
    amount DOUBLE NOT NULL,
    date DATE NOT NULL,
    note VARCHAR(255),
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE incomes (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    source VARCHAR(255) NOT NULL,     -- Source of income (e.g., Salary, Freelance)
    amount DOUBLE NOT NULL,           -- Amount of income
    date DATE NOT NULL,               -- Date of the income
    note VARCHAR(255),                -- Optional note for the income
    user_id BIGINT NOT NULL,          -- Foreign key linking the income to a user
    CONSTRAINT fk_user_income FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE budget (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(255) NOT NULL,
    allocated_amount DECIMAL(15, 2) NOT NULL,
    spent_amount DECIMAL(15, 2) NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE financial_goal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    target_amount DECIMAL(19, 2) NOT NULL,
    current_amount DECIMAL(19, 2) DEFAULT 0.00 NOT NULL,
    due_date DATE NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    completion_date DATE,
    user_id BIGINT,
    CONSTRAINT fk_user_goal FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE CurrencyConversionHistory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(15, 2) NOT NULL,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    converted_amount DECIMAL(15, 2) NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_currency FOREIGN KEY (user_id) REFERENCES users(id),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE future_investment_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    monthly_investment DECIMAL(15, 2) NOT NULL,
    investment_period INT NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    expected_rate DECIMAL(5, 2) NOT NULL,
    future_value DECIMAL(15, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    CONSTRAINT fk_investment FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);


CREATE TABLE loan_calculation_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    principal DECIMAL(15, 2) NOT NULL,
    annual_rate DECIMAL(5, 2) NOT NULL,
    tenure_in_years INT NOT NULL,
    emi DECIMAL(15, 2) NOT NULL,
    total_payment DECIMAL(15, 2) NOT NULL,
    total_interest DECIMAL(15, 2) NOT NULL,
    user_id BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_loan FOREIGN KEY (user_id) REFERENCES users(id)
);
