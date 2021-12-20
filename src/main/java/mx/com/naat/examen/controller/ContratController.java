package mx.com.naat.examen.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.naat.examen.domain.api.ContratServicePort;
import mx.com.naat.examen.domain.data.AccessTokenDto;
import mx.com.naat.examen.domain.data.ContratDetailDtoToSend;
import mx.com.naat.examen.domain.data.ContratRequestBody;
import mx.com.naat.examen.domain.data.CreateSignDto;
import mx.com.naat.examen.infrastructure.entity.Contrat;

@RestController
@RequestMapping("/fad/")
public class ContratController {

	@Autowired
	private ContratServicePort contratServicePort;

	@GetMapping("procesos")
	public ResponseEntity<Iterable<Contrat>> getUsers(@RequestParam(required = false, defaultValue = "0") int page,
													  @RequestParam(required = false, defaultValue = "20") int size) {
		Iterable<Contrat> contratos = contratServicePort.getAllContrats(page, size);
		
		HttpHeaders hr = new HttpHeaders();
		long totalElements = contratServicePort.countRegisters();
		hr.add("Total-Elements", String.valueOf(totalElements));
		
		System.out.println(totalElements);
		
		return new ResponseEntity<>(contratos, hr, HttpStatus.OK);
	}

	@GetMapping("detalle/{id}")
	public ResponseEntity<ContratDetailDtoToSend> getUserById(@PathVariable UUID id) {
		return new ResponseEntity<>(contratServicePort.getContratById(id), HttpStatus.OK);
	}

	 
	 @PostMapping("login")
	 public ResponseEntity<AccessTokenDto> loginWithFad(){
		 return new ResponseEntity<>(contratServicePort.LoginWithFad(), HttpStatus.OK);
	 }
	 
	 @PostMapping("createSign")
	 public ResponseEntity<CreateSignDto> createSign(@RequestBody ContratRequestBody contratRequestBody){
		 return new ResponseEntity<>(contratServicePort.postCreateSign(contratRequestBody), HttpStatus.OK);
	 }

}
