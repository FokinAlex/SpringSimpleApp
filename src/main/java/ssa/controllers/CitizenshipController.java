package ssa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssa.models.wrappers.Wrapper;
import ssa.services.CitizenshipService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/citizenship")
public class CitizenshipController {

    private final CitizenshipService citizenshipService;

    @Autowired
    public CitizenshipController(CitizenshipService citizenshipService) {
        this.citizenshipService = citizenshipService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper index() {
        return citizenshipService.getAllCitizenships();
    }
}
