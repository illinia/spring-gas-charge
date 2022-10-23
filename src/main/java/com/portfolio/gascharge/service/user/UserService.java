package com.portfolio.gascharge.service.user;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.domain.user.search.UserSearchStatus;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        Optional<User> byId = userRepository.findByEmail(email);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(User.class, email);
        }

        return byId.get();
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(UserSearchStatus userSearchStatus, Pageable pageable) {
        return userRepository.findUserWithSearchStatus(userSearchStatus, pageable);
    }
}
