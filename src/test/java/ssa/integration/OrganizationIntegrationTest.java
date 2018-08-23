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
import ssa.models.entities.Organization;
import ssa.repositories.OrganizationRepository;
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
public class OrganizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    @After
    public void clearRepository() {
        organizationRepository.deleteAll();
    }

    @Test
    public void givenOrganizationIndexUri_whenMockMvc_thenDataValueIsNull() throws Exception {

        // When:
        ResultActions resultActions = mockMvc.perform(get("/organization/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
    }

    @Test
    public void givenOrganizationIndexUri_whenMockMvc_thenResponseIsOk() throws Exception {

        // Given:
        fillDatabase();
        List<String> names = Arrays.stream(DBTestOrganizations.values())
                .map(DBTestOrganizations::getName)
                .collect(Collectors.toList());

        // When:
        ResultActions resultActions = mockMvc.perform(get("/organization/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(DBTestOrganizations.values().length)));

        for (int i = 0; i < DBTestOrganizations.values().length; i++) {
            resultActions.andExpect(jsonPath("$.data[" + i + "].name", isIn(names)));
        }
    }

    @Test
    public void givenOrganizationIdUri_whenMockMvc_thenResponseIsOk() throws Exception {

        // Given:
        Organization organization = DBTestOrganizations.ORGANIZATION_1.get();
        organizationRepository.save(organization);

        // When:
        ResultActions resultActions = mockMvc.perform(get("/organization/" + organization.getId()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(organization.getId()))
                .andExpect(jsonPath("$.data.name").value(DBTestOrganizations.ORGANIZATION_1.getName()))
                .andExpect(jsonPath("$.data.fullName").value(DBTestOrganizations.ORGANIZATION_1.getFullName()))
                .andExpect(jsonPath("$.data.inn").value(DBTestOrganizations.ORGANIZATION_1.getInn()))
                .andExpect(jsonPath("$.data.kpp").value(DBTestOrganizations.ORGANIZATION_1.getKpp()))
                .andExpect(jsonPath("$.data.address").value(DBTestOrganizations.ORGANIZATION_1.getAddress()))
                .andExpect(jsonPath("$.data.phone").value(DBTestOrganizations.ORGANIZATION_1.getPhone()))
                .andExpect(jsonPath("$.data.isActive").value(DBTestOrganizations.ORGANIZATION_1.isActive()));
    }

    @Test
    public void givenOrganizationIdUriWithIllegalId_whenMockMvc_thenErrorResponseIsOk() throws Exception {

        // When:
        ResultActions resultActions = mockMvc.perform(get("/organization/-1"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Organization with id = -1 cannot be found"));
    }

    @Test
    public void givenOrganizationListUri_whenMockMvc_thenResponseWithSingleObjectIsOk() throws Exception {

        // Given:
        Map<String, Organization> databaseEntities = fillDatabase();
        Organization organization = databaseEntities.get("organization2");

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", DBTestOrganizations.ORGANIZATION_2.getName());
        requestBody.put("inn", DBTestOrganizations.ORGANIZATION_2.getInn());
        requestBody.put("isActive", DBTestOrganizations.ORGANIZATION_2.isActive());

        // When:
        ResultActions resultActions = mockMvc.perform(post("/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(organization.getId()))
                .andExpect(jsonPath("$.data.name").value(DBTestOrganizations.ORGANIZATION_2.getName()))
                .andExpect(jsonPath("$.data.isActive").value(DBTestOrganizations.ORGANIZATION_2.isActive()));
    }

    @Test
    public void givenOrganizationUpdateUri_whenMockMvc_thenResponseIsOkAndRepositoryHasUpdatedData() throws Exception {

        // Given:
        Organization organization = DBTestOrganizations.ORGANIZATION_1.get();
        organizationRepository.save(organization);

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", organization.getId());
        requestBody.put("name", DBTestOrganizations.ORGANIZATION_NEW_NAME);
        requestBody.put("fullName", DBTestOrganizations.ORGANIZATION_NEW_FULLNAME);
        requestBody.put("inn", DBTestOrganizations.ORGANIZATION_NEW_INN);
        requestBody.put("kpp", DBTestOrganizations.ORGANIZATION_NEW_KPP);
        requestBody.put("address", DBTestOrganizations.ORGANIZATION_NEW_ADDRESS);
        requestBody.put("phone", DBTestOrganizations.ORGANIZATION_NEW_PHONE);

        // When:
        ResultActions resultActions = mockMvc.perform(post("/organization/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));

        Optional<Organization> optionalOffice = organizationRepository.findById(organization.getId());
        organization = optionalOffice.get();

        if (!organization.getName().equals(DBTestOrganizations.ORGANIZATION_NEW_NAME) ||
                !organization.getFullName().equals(DBTestOrganizations.ORGANIZATION_NEW_FULLNAME) ||
                !organization.getInn().equals(DBTestOrganizations.ORGANIZATION_NEW_INN) ||
                !organization.getKpp().equals(DBTestOrganizations.ORGANIZATION_NEW_KPP) ||
                !organization.getAddress().equals(DBTestOrganizations.ORGANIZATION_NEW_ADDRESS) ||
                !organization.getPhone().equals(DBTestOrganizations.ORGANIZATION_NEW_PHONE)) {
            throw new Exception("Organization is not updated");
        }
    }

    @Test
    public void givenOrganizationSaveUri_whenMockMvc_thenResponseIsOkAndRepositoryHasSavedData() throws Exception {

        // Given:
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", DBTestOrganizations.ORGANIZATION_1.getName());
        requestBody.put("fullName", DBTestOrganizations.ORGANIZATION_1.getFullName());
        requestBody.put("inn", DBTestOrganizations.ORGANIZATION_1.getInn());
        requestBody.put("kpp", DBTestOrganizations.ORGANIZATION_1.getKpp());
        requestBody.put("address", DBTestOrganizations.ORGANIZATION_1.getAddress());
        requestBody.put("phone", DBTestOrganizations.ORGANIZATION_1.getPhone());
        requestBody.put("isActive", DBTestOrganizations.ORGANIZATION_1.isActive());

        // When:
        ResultActions resultActions = mockMvc.perform(post("/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));

        List<Organization> optionalOrganization = organizationRepository.findAll();
        Organization organization = optionalOrganization.get(0);
        if (!organization.getName().equals(DBTestOrganizations.ORGANIZATION_1.getName()) ||
                !organization.getFullName().equals(DBTestOrganizations.ORGANIZATION_1.getFullName()) ||
                !organization.getInn().equals(DBTestOrganizations.ORGANIZATION_1.getInn()) ||
                !organization.getKpp().equals(DBTestOrganizations.ORGANIZATION_1.getKpp()) ||
                !organization.getAddress().equals(DBTestOrganizations.ORGANIZATION_1.getAddress()) ||
                !organization.getPhone().equals(DBTestOrganizations.ORGANIZATION_1.getPhone())) {
            throw new Exception("Organization is not saved");
        }
    }

    private Map<String, Organization> fillDatabase() {
        Organization organization1 = DBTestOrganizations.ORGANIZATION_1.get();
        Organization organization2 = DBTestOrganizations.ORGANIZATION_2.get();
        Organization organization3 = DBTestOrganizations.ORGANIZATION_3.get();

        Map<String, Organization> result = new HashMap<>();
        result.put("organization1", (Organization) organizationRepository.save(organization1));
        result.put("organization2", (Organization) organizationRepository.save(organization2));
        result.put("organization3", (Organization) organizationRepository.save(organization3));
        return result;
    }
}
