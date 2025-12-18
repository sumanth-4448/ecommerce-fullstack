package com.ecommerce.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

}
