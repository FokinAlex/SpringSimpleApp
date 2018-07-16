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

    public Wrapper getAllCitizenships() {
        try {
            List<Citizenship> citizenships =  citizenshipRepository.findAll();
            DataWrapper result = new DataWrapper();

            citizenships.forEach(citizenship -> {
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
