package ssa.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ssa.SsaApplication;
import ssa.repositories.DocumentRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SsaApplication.class)
@AutoConfigureMockMvc
public class DocumentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void rightCountOfDocumentTypes() throws Exception {

        // Given:
        int countOfDocumentTypes = (int) documentRepository.count();

        // When:
        ResultActions resultActions = mockMvc.perform(get("/document/").contentType(MediaType.APPLICATION_JSON));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data",  hasSize(countOfDocumentTypes)));
    }
}