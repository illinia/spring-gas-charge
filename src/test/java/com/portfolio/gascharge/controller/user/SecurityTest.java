package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.enums.user.UserAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_EMAIL1;
import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_NAME1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getUserInfoWithoutAuthorization() throws Exception {
        mvc.perform(get("/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@test.com")
    void getUserInfoWithAuthorization() throws Exception {
        mvc.perform(get("/user/me"))
                .andExpect(status().isOk())
        ;
    }


}
