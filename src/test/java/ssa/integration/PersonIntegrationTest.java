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
import ssa.models.entities.Person;
import ssa.repositories.OfficeRepository;
import ssa.repositories.PersonRepository;
import ssa.utils.DBTestOffices;
import ssa.utils.DBTestPersons;

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
public class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private PersonRepository personRepository;

    @After
    public void clearRepository() {
        personRepository.deleteAll();
        officeRepository.deleteAll();
    }

    @Test
    public void givenPersonIndexUri_whenMockMvc_thenDataValueIsNull() throws Exception {

        // When:
        ResultActions resultActions = mockMvc.perform(get("/person/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
    }

    @Test
    public void givenPersonIndexUri_whenMockMvc_thenResponseIsOk() throws Exception {

        // Given:
        fillDatabase();
        List<String> names = Arrays.stream(DBTestPersons.values())
                .map(DBTestPersons::getFirstName)
                .collect(Collectors.toList());

        // When:
        ResultActions resultActions = mockMvc.perform(get("/person/"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(DBTestPersons.values().length)));

        for (int i = 0; i < DBTestPersons.values().length; i++) {
            resultActions.andExpect(jsonPath("$.data[" + i + "].firstName", isIn(names)));
        }
    }

    @Test
    public void givenPersonIdUri_whenMockMvc_thenResponseIsOk() throws Exception {

        // Given:
        Person person = DBTestPersons.PERSON_1.get();
        personRepository.save(person);

        // When:
        ResultActions resultActions = mockMvc.perform(get("/person/" + person.getId()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(person.getId()))
                .andExpect(jsonPath("$.data.firstName").value(DBTestPersons.PERSON_1.getFirstName()))
                .andExpect(jsonPath("$.data.middleName").value(DBTestPersons.PERSON_1.getMiddleName()))
                .andExpect(jsonPath("$.data.secondName").value(DBTestPersons.PERSON_1.getSecondName()))
                .andExpect(jsonPath("$.data.position").value(DBTestPersons.PERSON_1.getPosition()))
                .andExpect(jsonPath("$.data.isIdentified").value(DBTestPersons.PERSON_1.isIdentified()));
    }

    @Test
    public void givenPersonIdUriWithIllegalId_whenMockMvc_thenErrorResponseIsOk() throws Exception {

        // When:
        ResultActions resultActions = mockMvc.perform(get("/person/-1"));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Person with id = -1 cannot be found"));
    }

    @Test
    public void givenPersonListUri_whenMockMvc_thenResponseWithSingleObjectIsOk() throws Exception {

        // Given:
        Map<String, Object> databaseEntities = fillDatabase();
        Office office = (Office) databaseEntities.get("office2");
        Person person = (Person) databaseEntities.get("person3");

        JSONObject requestBody = new JSONObject();
        requestBody.put("officeId", office.getId());

        // When:
        ResultActions resultActions = mockMvc.perform(post("/person/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(person.getId()))
                .andExpect(jsonPath("$.data.firstName").value(DBTestPersons.PERSON_3.getFirstName()))
                .andExpect(jsonPath("$.data.middleName").value(DBTestPersons.PERSON_3.getMiddleName()))
                .andExpect(jsonPath("$.data.secondName").value(DBTestPersons.PERSON_3.getSecondName()))
                .andExpect(jsonPath("$.data.position").value(DBTestPersons.PERSON_3.getPosition()));
    }

    @Test
    public void givenPersonUpdateUri_whenMockMvc_thenResponseIsOkAndRepositoryHasUpdatedData() throws Exception {

        // Given:
        Person person = DBTestPersons.PERSON_1.get();
        personRepository.save(person);

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", person.getId());
        requestBody.put("firstName", DBTestPersons.PERSON_NEW_FIRSTNAME);
        requestBody.put("position", DBTestPersons.PERSON_NEW_POSITION);

        // When:
        ResultActions resultActions = mockMvc.perform(post("/person/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));

        Optional<Person> optionalPerson = personRepository.findById(person.getId());
        person = optionalPerson.get();
        if (!person.getFirstName().equals(DBTestPersons.PERSON_NEW_FIRSTNAME) || !person.getPosition().equals(DBTestPersons.PERSON_NEW_POSITION)) {
            throw new Exception("Person is not updated");
        }
    }

    @Test
    public void givenPersonSaveUri_whenMockMvc_thenResponseIsOkAndRepositoryHasSavedData() throws Exception {

        // Given:
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstName", DBTestPersons.PERSON_1.getFirstName());
        requestBody.put("position", DBTestPersons.PERSON_1.getPosition());

        // When:
        ResultActions resultActions = mockMvc.perform(post("/person/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()));

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));

        List<Person> optionalOffice = personRepository.findAll();
        Person person = optionalOffice.get(0);
        if (!person.getFirstName().equals(DBTestPersons.PERSON_1.getFirstName()) || !person.getPosition().equals(DBTestPersons.PERSON_1.getPosition())) {
            throw new Exception("Person is not saved");
        }
    }

    private Map<String, Object> fillDatabase() {
        Office office1 = DBTestOffices.OFFICE_1.get();
        Office office2 = DBTestOffices.OFFICE_2.get();
        Person person1 = DBTestPersons.PERSON_1.get();
        Person person2 = DBTestPersons.PERSON_2.get();
        Person person3 = DBTestPersons.PERSON_3.get();
        person1.setOffice(office1);
        person2.setOffice(office1);
        person3.setOffice(office2);

        Map<String, Object> result = new HashMap<>();
        result.put("office1", officeRepository.save(office1));
        result.put("office2", officeRepository.save(office2));
        result.put("person1", personRepository.save(person1));
        result.put("person2", personRepository.save(person2));
        result.put("person3", personRepository.save(person3));
        return result;
    }
}
