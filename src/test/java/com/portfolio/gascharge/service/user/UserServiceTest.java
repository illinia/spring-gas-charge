package com.portfolio.gascharge.service.user;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.domain.user.UserTestData;
import com.portfolio.gascharge.domain.user.search.UserSearchStatus;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private User user;
    private User admin;

    @BeforeEach
    void setUser() {
        this.user = UserTestData.getCloneUser();
        this.admin = UserTestData.getCloneAdmin();
    }

    @Test
    void findById() {
        // given user id
        String email = this.user.getEmail();

        // when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(this.user));

        // then
        assertThat(userService.findByEmail(email)).isEqualTo(this.user);

        // when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.findByEmail(email))
                .isInstanceOf(NoEntityFoundException.class);
    }

    @Test
    void findAll() {
        // given user, admin, page 0, size 10, email desc
        UserSearchStatus userSearchStatus = new UserSearchStatus();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by("email").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<User> users = new ArrayList<>();
        users.add(this.user);
        users.add(this.admin);
        Page mockPage = new PageImpl<>(users, pageRequest, users.size());

        // when
        when(userRepository.findUserWithSearchStatus(any(UserSearchStatus.class), any(Pageable.class)))
                .thenReturn(mockPage);
        Page<User> result = userService.findAll(userSearchStatus, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(page);
        assertThat(result.getPageable().getSort()).isEqualTo(sort);
        assertThat(result.getContent().get(0)).isEqualTo(this.user);
        assertThat(result.getContent().get(1)).isEqualTo(this.admin);
    }
}
