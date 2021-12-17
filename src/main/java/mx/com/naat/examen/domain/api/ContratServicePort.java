package mx.com.naat.examen.domain.api;

import java.util.UUID;

import mx.com.naat.examen.domain.data.AccessTokenDto;
import mx.com.naat.examen.domain.data.ContratDetailDtoToSend;
import mx.com.naat.examen.domain.data.ContratRequestBody;
import mx.com.naat.examen.domain.data.CreateSignDto;
import mx.com.naat.examen.infrastructure.entity.Contrat;

public interface ContratServicePort {
	
	AccessTokenDto LoginWithFad();
	
	Iterable<Contrat> getAllContrats(int page, int size);

	ContratDetailDtoToSend getContratById(UUID id);
	
	CreateSignDto postCreateSign(ContratRequestBody contrat);
	
	long countRegisters();
}
