package com.dwag1n.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class TxtAnalyzerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAnalyzeTxtFreqWithLargeFileWithoutAuthentication() throws Exception {
        String txtFileName = "test.txt";
        int k = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/txt-analyzer/get/largeFile")
                        .param("txtFileName", txtFileName)
                        .param("k", String.valueOf(k)))
                .andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAnalyzeTxtFreqWithLargeFileWithAuthenticationButFileNotExists() throws Exception {
        String txtFileName = "test.txt";
        int k = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/txt-analyzer/get/largeFile")
                        .param("txtFileName", txtFileName)
                        .param("k", String.valueOf(k)))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }
}