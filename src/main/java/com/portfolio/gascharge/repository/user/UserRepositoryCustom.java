package com.portfolio.gascharge.repository.user;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.domain.user.search.UserSearchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> findUserWithSearchStatus(UserSearchStatus userSearchStatus, Pageable pageable);
}
