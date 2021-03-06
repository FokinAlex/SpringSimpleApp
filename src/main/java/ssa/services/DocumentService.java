package ssa.services;

import org.springframework.stereotype.Service;
import ssa.models.entities.Document;
import ssa.models.wrappers.*;
import ssa.repositories.DocumentRepository;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Returns a {@link Wrapper} which contains information about all document's types.
     * Next information are available:
     * <ul>
     *     <li>code: type's code;</li>
     *     <li>name: type's name.</li>
     * </ul>
     *
     * @return the {@link Wrapper} object that can then be transformed into JSON
     */
    public Wrapper getAllDocuments() {
        try {
            List<Document> documents =  documentRepository.findAll();
            DataWrapper result = new DataWrapper();

            documents.forEach(document -> {
                result.getContentBuilder()
                        .put("code", document.getCode())
                        .put("name", document.getName())
                        .put();
            });

            return result;
        } catch (Exception e) {
            return new ErrorWrapper(e.getMessage());
        }
    }
}