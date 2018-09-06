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

    /**
     * Returns a {@link Wrapper} which contains information about all {@link Office}s.
     * Next information are available:
     * <ul>
     *     <li>id: office's id;</li>
     *     <li>name: office's name;</li>
     *     <li>isActive: boolean value of office's activity.</li>
     * </ul>
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Returns a {@link Wrapper} which contains information about {@link Office} with defined id.
     * Next information are available:
     * <ul>
     *     <li>id: office's id;</li>
     *     <li>name: office's name;</li>
     *     <li>address: office's address;</li>
     *     <li>phone: office's phone;</li>
     *     <li>isActive: boolean value of office's activity.</li>
     * </ul>
     *
     * @param id the office's id {@link String} value which must be convertible to {@link Long}
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Returns a {@link Wrapper} which contains information about {@link Office}s that were found by defined filters.
     * Next values can be in filters:
     * <ul>
     *     <li>orgId: organization's id - required;</li>
     *     <li>name: office's name - optional;</li>
     *     <li>phone: office's phone - optional;</li>
     *     <li>isActive: boolean value of office's activity - optional.</li>
     * </ul>
     *
     * Next information are available:
     * <ul
     *     <li>id: office's id;</li>
     *     <li>name: office's name;</li>
     *     <li>isActive: boolean value of office's activity.</li>
     * </ul>
     *
     * @param filters the {@link String} value which contains filters and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
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

    /**
     * Updates an {@link Office} and returns a {@link Wrapper} which contains information about operation results.
     * Next values can be in argument:
     * <ul>
     *     <li>id: office's id - required;</li>
     *     <li>name: office's name - required;</li>
     *     <li>address: office's address - required;</li>
     *     <li>phone: office's phone - optional;</li>
     *     <li>isActive: boolean value of office's activity - optional.</li>
     * </ul>
     *
     * @param officeInfo the {@link String} value which contains information and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
    public Wrapper updateOffice(String officeInfo) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(officeInfo);
            if (null == jsonObject.get("id") ||
                    null == jsonObject.get("name") ||
                    null == jsonObject.get("address")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }
            long id = (long) jsonObject.get("id");
            Optional<Office> optionalOffice = officeRepository.findById(id);

            if (optionalOffice.isPresent()) {
                Office office = optionalOffice.get();

                office.setName((String) jsonObject.get("name"));
                office.setAddress((String) jsonObject.get("address"));
                if (null != jsonObject.get("phone")) office.setPhone((String) jsonObject.get("phone"));
                if (null != jsonObject.get("isActive")) office.setIsActive((Boolean) jsonObject.get("isActive"));

                officeRepository.save(office);
                // TODO: check if entity not updated ?
                ResultWrapper result = new ResultWrapper("success");
                return result;
            } else {
                throw new NoSuchElementException("Office with id = " + id + " cannot be found");
            }
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }

    /**
     * Saves an {@link Office} and returns a {@link Wrapper} which contains information about operation results.
     * Next values can be in argument:
     * <ul>
     *     <li>name: office's name - required;</li>
     *     <li>address: office's address - required;</li>
     *     <li>phone: office's phone - optional;</li>
     *     <li>isActive: boolean value of office's activity - optional.</li>
     * </ul>
     *
     * @param newOfficeInfo the {@link String} value which contains information and must be convertible into JSON
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
    public Wrapper saveOffice(String newOfficeInfo) {
        try {
            JSONObject jsonObject = (JSONObject) PARSER.parse(newOfficeInfo);
            if (null == jsonObject.get("name") ||
                    null == jsonObject.get("address")) {
                throw new IllegalArgumentException("Required parameter is missing");
            }

            Office office = new Office();
            office.setName((String) jsonObject.get("name"));
            office.setAddress((String) jsonObject.get("address"));
            if (null != jsonObject.get("phone")) office.setPhone((String) jsonObject.get("phone"));
            if (null != jsonObject.get("isActive")) office.setIsActive((Boolean) jsonObject.get("isActive"));

            officeRepository.save(office);
            // TODO: check if entity not saved ?
            ResultWrapper result = new ResultWrapper("success");
            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }
}
