package com.sakhee.finman.expenso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sakhee.finman.expenso.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByEmail(String email);
}

