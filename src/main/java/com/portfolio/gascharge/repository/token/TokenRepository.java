package com.portfolio.gascharge.repository.token;

import com.portfolio.gascharge.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
