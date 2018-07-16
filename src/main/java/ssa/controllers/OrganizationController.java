package ssa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssa.models.wrappers.Wrapper;
import ssa.services.OrganizationService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/organization")
public class OrganizationController implements SSAController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper index() {
        return organizationService.getAllOrganizations();
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper id(@PathVariable String id) {
        return organizationService.getOrganizationById(id);
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper list(@RequestBody String requestBody) {
        return organizationService.getOrganizationsByFilters(requestBody);
    }
}