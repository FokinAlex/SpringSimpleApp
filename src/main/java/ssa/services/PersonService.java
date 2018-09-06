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

    /**
     * Returns a {@link Wrapper} which contains information about all {@link Person}s.
     * Next information are available:
     * <ul>
     *     <li>id: person's id;</li>
     *     <li>firstName: person's first name;</li>
     *     <li>secondName: person's second name;</li>
     *     <li>middleName: person's middle name;</li>
     *     <li>position: person's position.</li>
     * </ul>
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Returns a {@link Wrapper} which contains information about {@link Person} with defined id.
     * Next information are available:
     * <ul>
     *     <li>id: person's id;</li>
     *     <li>firstName: person's first name;</li>
     *     <li>secondName: person's second name;</li>
     *     <li>middleName: person's middle name;</li>
     *     <li>position: person's position;</li>
     *     <li>phone: person's phone;</li>
     *     <li>docName: name of person's document;</li>
     *     <li>docCode: code of person's document;</li>
     *     <li>docDate: date of person's document;</li>
     *     <li>citizenshipName: name of person's citizenship;</li>
     *     <li>citizenshipCode: code of person's citizenship;</li>
     *     <li>isIdentified: boolean value of person's identity.</li>
     * </ul>
     *
     * @param id the office's id {@link String} value which must be convertible to {@link Long}
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Returns a {@link Wrapper} which contains information about {@link Person}s that were found by defined filters.
     * Next values can be in filters:
     * <ul>
     *     <li>officeId: office's id - required;</li>
     *     <li>firstName: person's first name - required;</li>
     *     <li>middleName: person's middle name - optional;</li>
     *     <li>position: person's position - required;</li>
     *     <li>docCode: code of person's document - optional;</li>
     *     <li>citizenshipCode: code of person's citizenship - optional.</li>
     * </ul>
     *
     * Next information are available:
     * <ul
     *     <li>id: person's id;</li>
     *     <li>firstName: person's first name;</li>
     *     <li>secondName: person's second name;</li>
     *     <li>middleName: person's middle name;</li>
     *     <li>position: person's position.</li>
     * </ul>
     *
     * @param filters the {@link String} value which contains filters and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Updates an {@link Person} and returns a {@link Wrapper} which contains information about operation results.
     * Next values can be in argument:
     * <ul>
     *     <li>id: person's id - required;</li>
     *     <li>firstName: person's first name - required;</li>
     *     <li>position: person's position - required;</li>
     *     <li>secondName: person's second name - optional;</li>
     *     <li>middleName: person's middle name - optional;</li>
     *     <li>phone: person's phone - optional;</li>
     *     <li>docCode: code of person's document - optional;</li>
     *     <li>docDate: date of person's document - optional;</li>
     *     <li>citizenshipCode: code of person's citizenship - optional;</li>
     *     <li>isIdentified: boolean value of person's identity - optional.</li>
     * </ul>
     *
     * @param personInfo the {@link String} value which contains information and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Saves an {@link Person} and returns a {@link Wrapper} which contains information about operation results.
     * Next values can be in argument:
     * <ul>
     *     <li>firstName: person's first name - required;</li>
     *     <li>position: person's position - required;</li>
     *     <li>secondName: person's second name - optional;</li>
     *     <li>middleName: person's middle name - optional;</li>
     *     <li>phone: person's phone - optional;</li>
     *     <li>docCode: code of person's document - optional;</li>
     *     <li>docDate: date of person's document - optional;</li>
     *     <li>citizenshipCode: code of person's citizenship - optional;</li>
     *     <li>isIdentified: boolean value of person's identity - optional.</li>
     * </ul>
     *
     * @param newPersonInfo the {@link String} value which contains information and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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
