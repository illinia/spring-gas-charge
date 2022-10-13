package com.portfolio.gascharge.service.user;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> byId = userRepository.findById(id);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(User.class, id.toString());
        }

        return byId.get();
    }
}
