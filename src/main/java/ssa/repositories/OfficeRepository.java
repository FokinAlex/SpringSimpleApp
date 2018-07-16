package ssa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ssa.models.entities.Office;

@Repository
public interface OfficeRepository<T extends Office, Id extends Long> extends JpaRepository<T, Id>, JpaSpecificationExecutor<T> {
}