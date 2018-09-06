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

    /**
     * Returns a {@link Wrapper} which contains information about all {@link Organization}s.
     * Next information are available:
     * <ul>
     *     <li>id: organization's id;</li>
     *     <li>name: organization's name;</li>
     *     <li>isActive: boolean value of organization's activity.</li>
     * </ul>
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Returns a {@link Wrapper} which contains information about {@link Organization} with defined id.
     * Next information are available:
     * <ul>
     *     <li>id: organization's id;</li>
     *     <li>name: organization's name;</li>
     *     <li>fullName: organization's full name;</li>
     *     <li>inn: organization's inn;</li>
     *     <li>kpp: organization's kpp;</li>
     *     <li>address: organization's address;</li>
     *     <li>phone: organization's phone;</li>
     *     <li>isActive: boolean value of organization's activity.</li>
     * </ul>
     *
     * @param id the organization's id {@link String} value which must be convertible to {@link Long}
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Returns a {@link Wrapper} which contains information about {@link Organization}s that were found by defined filters.
     * Next values can be in filters:
     * <ul>
     *     <li>name: organization's name - required;</li>
     *     <li>inn: organization's inn - optional;</li>
     *     <li>isActive: boolean value of organization's activity - optional.</li>
     * </ul>
     *
     * Next information are available:
     * <ul
     *     <li>id: organization's id;</li>
     *     <li>name: organization's name;</li>
     *     <li>isActive: boolean value of organization's activity.</li>
     * </ul>
     *
     * @param filters the {@link String} value which contains filters and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Updates an {@link Organization} and returns a {@link Wrapper} which contains information about operation results.
     * Next values can be in argument:
     * <ul>
     *     <li>id: organization's id - required;</li>
     *     <li>name: organization's name - required;</li>
     *     <li>fullName: organization's full name - required;</li>
     *     <li>inn: organization's inn - required;</li>
     *     <li>kpp: organization's kpp - required;</li>
     *     <li>address: organization's address - required;</li>
     *     <li>phone: organization's phone - optional;</li>
     *     <li>isActive: boolean value of organization's activity - optional.</li>
     * </ul>
     *
     * @param organizationInfo the {@link String} value which contains information and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Saves an {@link Organization} and returns a {@link Wrapper} which contains information about operation results.
     * Next values can be in argument:
     * <ul>
     *     <li>name: organization's name - required;</li>
     *     <li>fullName: organization's full name - required;</li>
     *     <li>inn: organization's inn - required;</li>
     *     <li>kpp: organization's kpp - required;</li>
     *     <li>address: organization's address - required;</li>
     *     <li>phone: organization's phone - optional;</li>
     *     <li>isActive: boolean value of organization's activity - optional.</li>
     * </ul>
     *
     * @param newOrganizationInfo the {@link String} value which contains information and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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
