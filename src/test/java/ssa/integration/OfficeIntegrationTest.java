package ssa.integration;

import org.hamcrest.core.IsNull;
import org.json.simple.JSONObject;
import org.junit.After;
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
import ssa.models.entities.Office;
import ssa.models.entities.Organization;
import ssa.repositories.OfficeRepository;
import ssa.repositories.OrganizationRepository;
import ssa.utils.DBTestOffices;
import ssa.utils.DBTestOrganizations;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SsaApplication.class)
@AutoConfigureMockMvc
public class OfficeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @After
    public void clearRepository() {
        officeRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void givenOfficeIndexUri_whenMockMvc_thenDataValueIsNull() throws Exception {

        // When:
        ResultActions resultActions = mockMvc.perform(get("/office/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
    }

    @Test
    public void givenOfficeIndexUri_whenMockMvc_thenResponseIsOk() throws Exception {

        // Given:
        fillDatabase();
        List<String> names = Arrays.stream(DBTestOffices.values())
                .map(DBTestOffices::getName)
                .collect(Collectors.toList());

        // When:
        ResultActions resultActions = mockMvc.perform(get("/office/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(DBTestOffices.values().length)));

        for (int i = 0; i < DBTestOffices.values().length; i++) {
            resultActions.andExpect(jsonPath("$.data[" + i + "].name", isIn(names)));
        }
    }

    @Test
    public void givenOfficeIdUri_whenMockMvc_thenResponseIsOk() throws Exception {

        // Given:
        Organization organization = DBTestOrganizations.ORGANIZATION_1.get();
        Office office = DBTestOffices.OFFICE_1.get();
        office.setOrganization(organization);
        organizationRepository.save(organization);
        officeRepository.save(office);

        // When:
        ResultActions resultActions = mockMvc.perform(get("/office/" + office.getId()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(office.getId()))
                .andExpect(jsonPath("$.data.name").value(DBTestOffices.OFFICE_1.getName()))
                .andExpect(jsonPath("$.data.address").value(DBTestOffices.OFFICE_1.getAddress()))
                .andExpect(jsonPath("$.data.phone").value(DBTestOffices.OFFICE_1.getPhone()))
                .andExpect(jsonPath("$.data.isActive").value(DBTestOffices.OFFICE_1.isActive()));
    }

    @Test
    public void givenOfficeIdUriWithIllegalId_whenMockMvc_thenErrorResponseIsOk() throws Exception {

        // When:
        ResultActions resultActions = mockMvc.perform(get("/office/-1"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Office with id = -1 cannot be found"));
    }

    @Test
    public void givenOfficeListUri_whenMockMvc_thenResponseWithSingleObjectIsOk() throws Exception {

        // Given:
        Map<String, Object> databaseEntities = fillDatabase();
        Organization organization = (Organization) databaseEntities.get("organization2");
        Office office = (Office) databaseEntities.get("office3");

        JSONObject requestBody = new JSONObject();
        requestBody.put("orgId", organization.getId());

        // When:
        ResultActions resultActions = mockMvc.perform(post("/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(office.getId()))
                .andExpect(jsonPath("$.data.name").value(DBTestOffices.OFFICE_3.getName()))
                .andExpect(jsonPath("$.data.isActive").value(DBTestOffices.OFFICE_3.isActive()));
    }

    @Test
    public void givenOfficeUpdateUri_whenMockMvc_thenResponseIsOkAndRepositoryHasUpdatedData() throws Exception {

        // Given:
        Organization organization = DBTestOrganizations.ORGANIZATION_1.get();
        Office office = DBTestOffices.OFFICE_1.get();
        office.setOrganization(organization);
        organizationRepository.save(organization);
        officeRepository.save(office);

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", office.getId());
        requestBody.put("name", DBTestOffices.OFFICE_NEW_NAME);
        requestBody.put("address",DBTestOffices.OFFICE_NEW_ADDRESS);

        // When:
        ResultActions resultActions = mockMvc.perform(post("/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));

        Optional<Office> optionalOffice = officeRepository.findById(office.getId());
        office = optionalOffice.get();
        if (!office.getName().equals(DBTestOffices.OFFICE_NEW_NAME) || !office.getAddress().equals(DBTestOffices.OFFICE_NEW_ADDRESS)) {
            throw new Exception("Office is not updated");
        }
    }

    @Test
    public void givenOfficeSaveUri_whenMockMvc_thenResponseIsOkAndRepositoryHasSavedData() throws Exception {

        // Given:
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", DBTestOffices.OFFICE_1.getName());
        requestBody.put("address", DBTestOffices.OFFICE_1.getAddress());
        requestBody.put("phone", DBTestOffices.OFFICE_1.getPhone());
        requestBody.put("isActive", DBTestOffices.OFFICE_1.isActive());

        // When:
        ResultActions resultActions = mockMvc.perform(post("/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));

        List<Office> optionalOffice = officeRepository.findAll();
        Office office = optionalOffice.get(0);
        if (!office.getName().equals(DBTestOffices.OFFICE_1.getName()) ||
                !office.getAddress().equals(DBTestOffices.OFFICE_1.getAddress()) ||
                !office.getPhone().equals(DBTestOffices.OFFICE_1.getPhone()) ||
                office.isActive() != DBTestOffices.OFFICE_1.isActive()) {
            throw new Exception("Office is not saved");
        }
    }

    private Map<String, Object> fillDatabase() {
        Organization organization1 = DBTestOrganizations.ORGANIZATION_1.get();
        Organization organization2 = DBTestOrganizations.ORGANIZATION_2.get();
        Organization organization3 = DBTestOrganizations.ORGANIZATION_3.get();
        Office office1 = DBTestOffices.OFFICE_1.get();
        Office office2 = DBTestOffices.OFFICE_2.get();
        Office office3 = DBTestOffices.OFFICE_3.get();
        office1.setOrganization(organization1);
        office2.setOrganization(organization1);
        office3.setOrganization(organization2);

        Map<String, Object> result = new HashMap<>();
        result.put("organization1", organizationRepository.save(organization1));
        result.put("organization2", organizationRepository.save(organization2));
        result.put("organization3", organizationRepository.save(organization3));
        result.put("office1", officeRepository.save(office1));
        result.put("office2", officeRepository.save(office2));
        result.put("office3", officeRepository.save(office3));
        return result;
    }
}
