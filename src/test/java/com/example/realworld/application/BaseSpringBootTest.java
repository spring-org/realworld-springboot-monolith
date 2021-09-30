package com.example.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseSpringBootTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;
    protected MockHttpSession session;

    protected String makeSlug(String title) {
        return String.format(
                "%s-%s"
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , title);
    }
}
