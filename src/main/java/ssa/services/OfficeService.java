package ssa.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import ssa.models.entities.Office;
import ssa.models.wrappers.*;
import ssa.repositories.OfficeRepository;
import ssa.tools.FilterSpecificationBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OfficeService {

    private static final JSONParser PARSER = new JSONParser();
    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    public Wrapper getAllOffices() {
        try {
            List<Office> offices = this.officeRepository.findAll();

            DataWrapper result = new DataWrapper();

            offices.forEach(office -> {
                result.getContentBuilder()
                        .put("id", office.getId())
                        .put("name", office.getName())
                        .put("isActive", office.isActive())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper getOfficeById(String id) {
        try {
            Optional<Office> optionalOffice = officeRepository.findById(Long.parseLong(id));
            if (optionalOffice.isPresent()) {
                Office office = optionalOffice.get();
                DataWrapper result = new DataWrapper();

                result.getContentBuilder()
                        .put("id", office.getId())
                        .put("name", office.getName())
                        .put("address", office.getAddress())
                        .put("phone", office.getPhone())
                        .put("isActive", office.isActive())
                        .put();

                return result;
            } else {
                throw new NoSuchElementException("Office with id = " + id + " cannot be found");
            }
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper getOfficesByFilters(String filters) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(filters);
            if (null == jsonObject.get("orgId")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }
            long id = (long) jsonObject.get("orgId");

            List offices =  officeRepository.findAll(
                    new FilterSpecificationBuilder()
                            .addFilter("organizationId", id)
                            .addFilter("name", jsonObject.get("name"))
                            .addFilter("phone", jsonObject.get("phone"))
                            .addFilter("isActive", jsonObject.get("isActive"))
                            .build());

            DataWrapper result = new DataWrapper();

            offices.forEach(office -> {
                result.getContentBuilder()
                        .put("id", ((Office) office).getId())
                        .put("name", ((Office) office).getName())
                        .put("isActive", ((Office) office).isActive())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }
}