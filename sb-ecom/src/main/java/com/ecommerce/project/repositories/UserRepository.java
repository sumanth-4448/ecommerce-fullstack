package com.ecommerce.project.repositories;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    boolean existsByUserName(@NotBlank @Size(min=3, max=20) String username);

    boolean existsByEmail(@NotBlank @Size(max=50) @Email String email);
}
