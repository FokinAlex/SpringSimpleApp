package ssa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssa.models.entities.Citizenship;

@Repository
public interface CitizenshipRepository<T extends Citizenship, Id extends Short> extends JpaRepository<T, Id> {
}