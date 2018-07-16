package ssa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssa.models.entities.Document;

@Repository
public interface DocumentRepository<T extends Document, Id extends Byte> extends JpaRepository<T, Id> {
}