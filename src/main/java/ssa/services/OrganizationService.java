package ssa.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import ssa.models.entities.Organization;
import ssa.models.wrappers.*;
import ssa.repositories.OrganizationRepository;
import ssa.tools.FilterSpecificationBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrganizationService {

    private static final JSONParser PARSER = new JSONParser();
    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Wrapper getAllOrganizations() {
        try {
            List<Organization> organizations = organizationRepository.findAll();

            DataWrapper result = new DataWrapper();

            organizations.forEach(organization -> {
                result.getContentBuilder()
                        .put("id", organization.getId())
                        .put("name", organization.getName())
                        .put("isActive", organization.isActive())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper getOrganizationById(String id) {
        try {
            Optional<Organization> optionalOrganization = organizationRepository.findById(Long.parseLong(id));
            if (optionalOrganization.isPresent()) {
                Organization organization = optionalOrganization.get();
                DataWrapper result = new DataWrapper();

                result.getContentBuilder()
                        .put("id", organization.getId())
                        .put("name", organization.getName())
                        .put("fullName", organization.getFullName())
                        .put("inn", organization.getInn())
                        .put("kpp", organization.getKpp())
                        .put("address", organization.getAddress())
                        .put("phone", organization.getPhone())
                        .put("isActive", organization.isActive())
                        .put();

                return result;
            } else {
                throw new NoSuchElementException("Organization with id = " + id + " cannot be found");
            }
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper getOrganizationsByFilters(String filters) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(filters);
            if (null == jsonObject.get("name")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }
            List organizations =  organizationRepository.findAll(
                    new FilterSpecificationBuilder()
                            .addFilter("name", jsonObject.get("name"))
                            .addFilter("inn", jsonObject.get("inn"))
                            .addFilter("isActive", jsonObject.get("isActive"))
                            .build());


            DataWrapper result = new DataWrapper();

            organizations.forEach(organization -> {
                result.getContentBuilder()
                        .put("id", ((Organization) organization).getId())
                        .put("name", ((Organization) organization).getName())
                        .put("isActive", ((Organization) organization).isActive())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper updateOrganization(String organizationInfo) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(organizationInfo);
            if (null == jsonObject.get("id") ||
                    null == jsonObject.get("name") ||
                    null == jsonObject.get("fullName") ||
                    null == jsonObject.get("inn") ||
                    null == jsonObject.get("kpp") ||
                    null == jsonObject.get("address")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }
            long id = (long) jsonObject.get("id");
            Optional<Organization> optionalOrganization = organizationRepository.findById(id);

            if (optionalOrganization.isPresent()) {
                Organization organization = optionalOrganization.get();

                organization.setName((String) jsonObject.get("name"));
                organization.setFullName((String) jsonObject.get("fullName"));
                organization.setInn((String) jsonObject.get("inn"));
                organization.setKpp((String) jsonObject.get("kpp"));
                organization.setAddress((String) jsonObject.get("address"));
                if (null != jsonObject.get("phone")) organization.setPhone((String) jsonObject.get("phone"));
                if (null != jsonObject.get("isActive")) organization.setIsActive((Boolean) jsonObject.get("isActive"));

                organizationRepository.save(organization);
                // TODO: check if entity not updated ?
                ResultWrapper result = new ResultWrapper("x");
                return result;
            } else {
                throw new NoSuchElementException("Organization with id = " + id + " cannot be found");
            }
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    public Wrapper saveOrganization(String newOrganizationInfo) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(newOrganizationInfo);
            if (null == jsonObject.get("name") ||
                    null == jsonObject.get("fullName") ||
                    null == jsonObject.get("inn") ||
                    null == jsonObject.get("kpp") ||
                    null == jsonObject.get("address")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }

            Organization organization = new Organization();
            organization.setName((String) jsonObject.get("name"));
            organization.setFullName((String) jsonObject.get("fullName"));
            organization.setInn((String) jsonObject.get("inn"));
            organization.setKpp((String) jsonObject.get("kpp"));
            organization.setAddress((String) jsonObject.get("address"));
            if (null != jsonObject.get("phone")) organization.setPhone((String) jsonObject.get("phone"));
            if (null != jsonObject.get("isActive")) organization.setIsActive((Boolean) jsonObject.get("isActive"));

            organizationRepository.save(organization);
            // TODO: check if entity not saved ?
            ResultWrapper result = new ResultWrapper("success");
            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }
}
