package org.library.management.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.library.management.services.LibraryService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected LibraryService libraryService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(libraryService);
    }

}
