package com.portfolio.gascharge.repository.user;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.domain.user.UserTestData;
import com.portfolio.gascharge.domain.user.search.UserSearchStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findUserWithSearchStatus() {
        // given
        User user = userRepository.save(UserTestData.getCloneUser());
        User admin = userRepository.save(UserTestData.getCloneAdmin());

        UserSearchStatus userSearchStatus = new UserSearchStatus();

        int page = 0;
        int size = 10;
        Sort sort = Sort.by("name").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // when
        Page<User> result = userRepository.findUserWithSearchStatus(userSearchStatus, pageRequest);

        // then
        assertThat(result.getPageable().getPageNumber()).isEqualTo(page);
        assertThat(result.getPageable().getPageSize()).isEqualTo(size);
        assertThat(result.getPageable().getSort()).isEqualTo(sort);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).toString()).isEqualTo(user.toString());
        assertThat(result.getContent().get(1).toString()).isEqualTo(admin.toString());

        // given
        userSearchStatus.setUserAuthority(user.getUserAuthority());

        // when
        Page<User> result1 = userRepository.findUserWithSearchStatus(userSearchStatus, pageRequest);

        // then
        assertThat(result1.getContent()).hasSize(1);
        assertThat(result1.getContent().get(0).toString()).isEqualTo(user.toString());

        // given
        userSearchStatus.setUserAuthority(null);
        userSearchStatus.setName(admin.getName());

        // when
        Page<User> result2 = userRepository.findUserWithSearchStatus(userSearchStatus, pageRequest);

        // then
        assertThat(result2.getContent()).hasSize(1);
        assertThat(result2.getContent().get(0).toString()).isEqualTo(admin.toString());

        // given
        userSearchStatus.setName(null);
        userSearchStatus.setEmailVerified(user.getEmailVerified());

        // when
        Page<User> result3 = userRepository.findUserWithSearchStatus(userSearchStatus, pageRequest);

        // then
        assertThat(result3.getContent()).hasSize(1);
        assertThat(result3.getContent().get(0).toString()).isEqualTo(user.toString());

        // given
        userSearchStatus.setEmailVerified(null);
        userSearchStatus.setEmail(admin.getEmail());

        // when
        Page<User> result4 = userRepository.findUserWithSearchStatus(userSearchStatus, pageRequest);

        // then
        assertThat(result4.getContent()).hasSize(1);
        assertThat(result4.getContent().get(0).toString()).isEqualTo(admin.toString());
    }

}