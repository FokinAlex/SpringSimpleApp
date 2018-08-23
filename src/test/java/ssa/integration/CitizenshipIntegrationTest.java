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
import ssa.repositories.CitizenshipRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SsaApplication.class)
@AutoConfigureMockMvc
public class CitizenshipIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CitizenshipRepository citizenshipRepository;

    @Test
    public void givenCitizenshipIndexUri_whenMockMvc_thenCountOfCitizenshipIsCorrect() throws Exception {

        // Given:
        int countOfCitizenship = (int) citizenshipRepository.count();

        // When:
        ResultActions resultActions = mockMvc.perform(get("/citizenship/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data",  hasSize(countOfCitizenship)));
    }
}