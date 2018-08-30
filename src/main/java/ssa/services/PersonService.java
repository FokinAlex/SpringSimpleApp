package ssa.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import ssa.models.entities.Person;
import ssa.models.wrappers.*;
import ssa.models.wrappers.Wrapper;
import ssa.repositories.PersonRepository;
import ssa.tools.FilterSpecificationBuilder;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonService {

    private static final JSONParser PARSER = new JSONParser();
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Wrapper getAllPersons() {
        try {
            List persons = this.personRepository.findAll();
            DataWrapper result = new DataWrapper();

            persons.forEach(person -> {
                result.getContentBuilder()
                        .put("id", ((Person) person).getId())
                        .put("firstName", ((Person) person).getFirstName())
                        .put("secondName", ((Person) person).getSecondName())
                        .put("middleName", ((Person) person).getMiddleName())
                        .put("position", ((Person) person).getPosition())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper getPersonById(String id) {
        try {
            Optional<Person> optionalPerson = personRepository.findById(Long.parseLong(id));
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                DataWrapper result = new DataWrapper();

                result.getContentBuilder()
                        .put("id", person.getId())
                        .put("firstName", person.getFirstName())
                        .put("secondName", person.getSecondName())
                        .put("middleName", person.getMiddleName())
                        .put("position", person.getPosition())
                        .put("phone", person.getPhone())
                        .put("docName", null != person.getDocument() ? person.getDocument().getName() : null)
                        .put("docCode", person.getDocCode())
                        .put("docDate", person.getDocDate())
                        .put("citizenshipName", null != person.getCitizenship() ? person.getCitizenship().getName() : null)
                        .put("citizenshipCode", person.getCitizenshipCode())
                        .put("isIdentified", person.isIdentified())
                        .put();

                return result;
            } else {
                throw new NoSuchElementException("Person with id = " + id + " cannot be found");
            }
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper getPersonsByFilters(String filters) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(filters);
            if (null == jsonObject.get("officeId")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }
            long id = (long) jsonObject.get("officeId");

            List persons =  personRepository.findAll(
                    new FilterSpecificationBuilder()
                            .addFilter("officeId", id)
                            .addFilter("firstName", jsonObject.get("firstName"))
                            .addFilter("middleName", jsonObject.get("middleName"))
                            .addFilter("position", jsonObject.get("position"))
                            .addFilter("docCode", jsonObject.get("docCode"))
                            .addFilter("citizenshipCode", jsonObject.get("citizenshipCode"))
                            .build());

            DataWrapper result = new DataWrapper();

            persons.forEach(person -> {
                result.getContentBuilder()
                        .put("id", ((Person) person).getId())
                        .put("firstName", ((Person) person).getFirstName())
                        .put("secondName", ((Person) person).getSecondName())
                        .put("middleName", ((Person) person).getMiddleName())
                        .put("position", ((Person) person).getPosition())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper updatePerson(String personInfo) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(personInfo);
            if (null == jsonObject.get("id") ||
                    null == jsonObject.get("firstName") ||
                    null == jsonObject.get("position")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }
            long id = (long) jsonObject.get("id");
            Optional<Person> optionalPerson = personRepository.findById(id);

            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();

                person.setFirstName((String) jsonObject.get("firstName"));
                person.setPosition((String) jsonObject.get("position"));
                if (null != jsonObject.get("secondName")) person.setSecondName((String) jsonObject.get("secondName"));
                if (null != jsonObject.get("middleName")) person.setMiddleName((String) jsonObject.get("middleName"));
                if (null != jsonObject.get("phone")) person.setPhone((String) jsonObject.get("phone"));
                if (null != jsonObject.get("docCode")) person.setDocCode((Byte) jsonObject.get("docCode"));
                if (null != jsonObject.get("docDate")) person.setDocDate(Date.valueOf((String) jsonObject.get("docDate")));
                if (null != jsonObject.get("citizenshipCode")) person.setCitizenshipCode((Short) jsonObject.get("citizenshipCode"));
                if (null != jsonObject.get("isIdentified")) person.setIsIdentified((Boolean) jsonObject.get("isIdentified"));

                personRepository.save(person);
                // TODO: check if entity not updated ?
                ResultWrapper result = new ResultWrapper("success");
                return result;
            } else {
                throw new NoSuchElementException("Person with id = " + id + " cannot be found");
            }
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper savePerson(String newPersonInfo) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(newPersonInfo);
            if (null == jsonObject.get("firstName") ||
                    null == jsonObject.get("position")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }

            Person person = new Person();
            person.setFirstName((String) jsonObject.get("firstName"));
            person.setPosition((String) jsonObject.get("position"));
            if (null != jsonObject.get("secondName")) person.setSecondName((String) jsonObject.get("secondName"));
            if (null != jsonObject.get("middleName")) person.setMiddleName((String) jsonObject.get("middleName"));
            if (null != jsonObject.get("phone")) person.setPhone((String) jsonObject.get("phone"));
            if (null != jsonObject.get("docCode")) person.setDocCode((Byte) jsonObject.get("docCode"));
            if (null != jsonObject.get("docDate")) person.setDocDate(Date.valueOf((String) jsonObject.get("docDate")));
            if (null != jsonObject.get("citizenshipCode")) person.setCitizenshipCode((Short) jsonObject.get("citizenshipCode"));
            if (null != jsonObject.get("isIdentified")) person.setIsIdentified((Boolean) jsonObject.get("isIdentified"));

            personRepository.save(person);
            // TODO: check if entity not saved ?
            ResultWrapper result = new ResultWrapper("success");
            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }
}
