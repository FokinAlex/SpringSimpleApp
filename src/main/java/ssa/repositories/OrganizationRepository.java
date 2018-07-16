package ssa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ssa.models.entities.Organization;

@Repository
public interface OrganizationRepository<T extends Organization, Id extends Long> extends JpaRepository<T, Id>, JpaSpecificationExecutor<T> {
}