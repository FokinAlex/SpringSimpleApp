package ssa.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ssa.models.wrappers.Wrapper;

@RestController
public interface SSAController {

    @ResponseBody 
    Wrapper index();

    @ResponseBody
    Wrapper id(@PathVariable String id);

    @ResponseBody
    Wrapper list(@RequestBody String requestBody);
}
