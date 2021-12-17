package mx.com.naat.examen.infrastructure.adapter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.com.naat.examen.domain.spi.ContratPersistancePort;
import mx.com.naat.examen.infrastructure.entity.Contrat;
import mx.com.naat.examen.infrastructure.repository.ContratRepository;

@Service
public class ContratJpaAdapter implements ContratPersistancePort {

	@Autowired
	private ContratRepository contratRepository;

	@Override
	public void saveContrat(Contrat contrat) {
		contratRepository.save(contrat);
	}

	@Override
	public List<Contrat> getAllContrats(Pageable pageable) {
		List<Contrat> contrats = new ArrayList<>();
		Iterable<Contrat> listaContrats = contratRepository.findAll(pageable);
		listaContrats.forEach(contrats::add);
		return contrats;
	}

	@Override
	public long countRegisters() {
		return contratRepository.count();
	}
}
