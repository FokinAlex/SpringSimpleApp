package ssa.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class DocumentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void givenDocumentIndexUri_whenMockMvc_thenCountOfDocumentTypesIsCorrect() throws Exception {

        // Given:
        int countOfDocumentTypes = (int) documentRepository.count();

        // When:
        ResultActions resultActions = mockMvc.perform(get("/document/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data",  hasSize(countOfDocumentTypes)));
    }
}