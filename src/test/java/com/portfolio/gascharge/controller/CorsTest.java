package com.portfolio.gascharge.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CorsTest {

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void testCorsFail() throws Exception {
        mvc.perform(
            options("/charge")
                    .header("Access-Control-Allow-Origin", "GET")
                    .header("Origin", "http://www.failtest.com"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testCors() throws Exception {
        mvc.perform(
                options("/charge")
                        .header("Access-Control-Allow-Origin","GET")
                        .header("Origin", "http://www.test.com"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://www.test.com"))
                .andExpect(status().isOk())
                ;

    }
}
