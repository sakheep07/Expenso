"# Expenso" 

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
    note VARCHAR(255)
);

ALTER TABLE expenses
ADD COLUMN user_id BIGINT NOT NULL,
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES users(id);


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
