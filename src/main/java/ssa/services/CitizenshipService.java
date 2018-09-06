package ssa.services;

import org.springframework.stereotype.Service;
import ssa.models.entities.Citizenship;
import ssa.models.wrappers.*;
import ssa.repositories.CitizenshipRepository;

import java.util.List;

@Service
public class CitizenshipService {

    private final CitizenshipRepository citizenshipRepository;

    public CitizenshipService(CitizenshipRepository citizenshipRepository) {
        this.citizenshipRepository = citizenshipRepository;
    }

    /**
     * Returns a {@link Wrapper} which contains information about all citizenship.
     * Next information are available:
     * <ul>
     *     <li>code: citizenship's code;</li>
     *     <li>name: citizenship's name.</li>
     * </ul>
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
    public Wrapper getAllCitizenship() {
        try {
            List<Citizenship> allCitizenship =  citizenshipRepository.findAll();
            DataWrapper result = new DataWrapper();

            allCitizenship.forEach(citizenship -> {
                result.getContentBuilder()
                        .put("code", citizenship.getCode())
                        .put("name", citizenship.getName())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }
}
