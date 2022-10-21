package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.enums.user.UserAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static com.portfolio.gascharge.domain.user.UserTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void getUserInfoWithoutAuthorization() throws Exception {
        mvc.perform(get("/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER_TEST_EMAIL)
    void getUserInfoWithAuthorization() throws Exception {
        mvc.perform(get("/user/me"))
                .andExpect(jsonPath("$.name").value(USER_TEST_NAME))
                .andExpect(jsonPath("$.email").value(USER_TEST_EMAIL))
                .andExpect(jsonPath("$.authority").value(UserAuthority.ROLE_USER.toString()))
                .andDo(print())
        ;
    }

    @Test
    @WithUserDetails(ADMIN_TEST_EMAIL)
    void getUserList() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserSearchStatus userSearchStatus = new UserSearchStatus();
//        List<User> testList = List.of(USER_TEST, ADMIN_TEST);
//        Page<User> users = new PageImpl<>(testList);
//        given(userService.findAll(userSearchStatus, pageable)).willReturn(users);

        mvc.perform(get("/user"))
                .andExpect(jsonPath("$.content[0].name").value(USER_TEST_NAME))
                .andExpect(jsonPath("$.content[0].email").value(USER_TEST_EMAIL))
                .andExpect(jsonPath("$.content[0].imageUrl").value(USER_TEST_IMAGE_URL))
                .andExpect(jsonPath("$.content[0].emailVerified").value(USER_TEST_EMAIL_VERIFIED.toString()))
                .andExpect(jsonPath("$.content[0].authProvider").value(USER_TEST_AUTH_PROVIDER.toString()))
                .andExpect(jsonPath("$.content[0].authority").value(USER_TEST_USER_AUTHORITY.toString()))

                .andExpect(jsonPath("$.content[1].name").value(ADMIN_TEST_NAME))
                .andExpect(jsonPath("$.content[1].email").value(ADMIN_TEST_EMAIL))
                .andExpect(jsonPath("$.content[1].imageUrl").value(ADMIN_TEST_IMAGE_URL))
                .andExpect(jsonPath("$.content[1].emailVerified").value(ADMIN_TEST_EMAIL_VERIFIED.toString()))
                .andExpect(jsonPath("$.content[1].authProvider").value(ADMIN_TEST_AUTH_PROVIDER.toString()))
                .andExpect(jsonPath("$.content[1].authority").value(ADMIN_TEST_USER_AUTHORITY.toString()))

                .andExpect(status().isOk())
        ;
    }
}
