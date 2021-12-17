package mx.com.naat.examen.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import mx.com.naat.examen.infrastructure.entity.Contrat;

@Repository
public interface ContratRepository extends PagingAndSortingRepository<Contrat, UUID> {

}