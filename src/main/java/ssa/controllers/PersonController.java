package ssa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssa.models.wrappers.Wrapper;
import ssa.services.PersonService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/person")
public class PersonController implements SSAController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper index() {
        return personService.getAllPersons();
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper id(@PathVariable String id) {
        return personService.getPersonById(id);
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper list(@RequestBody String requestBody) {
        return personService.getPersonsByFilters(requestBody);
    }
}
