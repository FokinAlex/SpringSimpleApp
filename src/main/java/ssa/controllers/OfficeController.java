package ssa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssa.models.wrappers.Wrapper;
import ssa.services.OfficeService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/office")
public class OfficeController implements SSAController {

    private final OfficeService officeService;

    @Autowired
    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper index() {
        return officeService.getAllOffices();
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper id(@PathVariable String id) {
        return officeService.getOfficeById(id);
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wrapper list(@RequestBody String requestBody) {
        return officeService.getOfficesByFilters(requestBody);
    }
}
