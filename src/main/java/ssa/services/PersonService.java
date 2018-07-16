package ssa.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import ssa.models.entities.Person;
import ssa.models.wrappers.DataWrapper;
import ssa.models.wrappers.ErrorWrapper;
import ssa.models.wrappers.Wrapper;
import ssa.repositories.PersonRepository;
import ssa.tools.FilterSpecificationBuilder;

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
                        .put("id", (person).getId())
                        .put("firstName", person.getFirstName())
                        .put("secondName", person.getSecondName())
                        .put("middleName", person.getMiddleName())
                        .put("position", person.getPosition())
                        .put("phone", person.getPhone())
                        .put("docName", person.getDocument().getName())
                        .put("docNumber", person.getDocCode())
                        .put("docDate", person.getDocDate())
                        .put("citizenshipName", person.getCitizenship().getName())
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
}
