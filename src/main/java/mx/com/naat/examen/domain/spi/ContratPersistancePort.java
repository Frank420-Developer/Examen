package mx.com.naat.examen.domain.spi;

import java.util.List;

import org.springframework.data.domain.Pageable;

import mx.com.naat.examen.infrastructure.entity.Contrat;

public interface ContratPersistancePort {
	List<Contrat> getAllContrats(Pageable pageable);
	void saveContrat(Contrat contrat);
	long countRegisters();
}
