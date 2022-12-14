package com.portfolio.gascharge.repository.user;

import com.portfolio.gascharge.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
