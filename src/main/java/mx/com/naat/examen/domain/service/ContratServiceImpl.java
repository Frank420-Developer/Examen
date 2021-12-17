package mx.com.naat.examen.domain.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mx.com.naat.examen.domain.data.AccessTokenDto;
import mx.com.naat.examen.domain.data.ChangeXmlDto;
import mx.com.naat.examen.domain.data.ContratDetailDto;
import mx.com.naat.examen.domain.data.ContratDetailDtoToSend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import mx.com.naat.examen.domain.api.ContratServicePort;
import mx.com.naat.examen.domain.data.ContratRequestBody;
import mx.com.naat.examen.domain.data.CreateSignDto;
import mx.com.naat.examen.domain.data.LoginResponseDto;
import mx.com.naat.examen.domain.data.SignersDto;
import mx.com.naat.examen.domain.data.SignersDtoToSend;
import mx.com.naat.examen.domain.spi.ContratPersistancePort;
import mx.com.naat.examen.infrastructure.entity.Contrat;

public class ContratServiceImpl implements ContratServicePort {

    private final ContratPersistancePort contratPersistancePort;
    
    @Value("${spring.apifad.urls.url-base}")
    String urlBase;
    
    @Value("${spring.apifad.urls.url-login}")
    String urlLogin;
    
    @Value("${spring.apifad.urls.url-createSign}")
    String urlCreate;
    
    @Value("${spring.apifad.urls.url-getDetail}")
    String urlGetDetail;

    @Value("${spring.apifad.doLogin.grantType}")
    String grantType;
    
    @Value("${spring.apifad.doLogin.username}")
    String username;
    
    @Value("${spring.apifad.doLogin.password}")
    String password;
    
    @Value("${spring.apifad.doLogin.basicAuth.username}")
    String basicAuthusername;
    
    @Value("${spring.apifad.doLogin.basicAuth.password}")
    String basicAuthpassword;
    
    @Value("${spring.apifad.createSign.xmlUrl}")
    String xmlUrl;
    
    @Value("${spring.apifad.createSign.pdfUrl}")
    String pdfUrl;
    
    @Value("${spring.apifad.createSign.hash}")
    String hash;
    
    public ContratServiceImpl(ContratPersistancePort contratPersistancePort) {
        this.contratPersistancePort = contratPersistancePort;
    }

    @Override
    public List<Contrat> getAllContrats(int page, int size) {
        
    	Pageable pageable = PageRequest.of(page, size);

        return (List<Contrat>) contratPersistancePort.getAllContrats(pageable);
    }
    
    @Override
	public ContratDetailDtoToSend getContratById(UUID id) {
    	String uri = urlBase+urlGetDetail+id;
    	
    	RestTemplate restTemplate = new RestTemplate();

    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setBasicAuth(basicAuthusername, basicAuthpassword);
		headers.setBearerAuth(LoginWithFad().getAccess_token());

    	ResponseEntity<ContratDetailDto> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers),ContratDetailDto.class);
    	
    	List<SignersDto> signers = response.getBody().getData().getSigners();
    	List<SignersDtoToSend> signersDto = new ArrayList<>();
    	
    	for(int i = 0; i < signers.size(); i++) {
    		signersDto.add(new SignersDtoToSend(signers.get(i).getSignerName(), signers.get(i).getMail(), signers.get(i).getPhone()));
    	}
    	
    	ContratDetailDtoToSend contratdetail = new ContratDetailDtoToSend(response.getBody().getData().getIdRequisitionClient(), 
    																	response.getBody().getData().getContractName(),
    																	response.getBody().getData().getOwnerName(),
    																	signersDto,
    																	response.getBody().getData().getStatus());
		return contratdetail;
	}
    
    @Override
    public AccessTokenDto LoginWithFad() { 
    	String uri = urlBase+urlLogin;
    	
    	RestTemplate restTemplate = new RestTemplate();
    	HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(
										createBodyToLogin(grantType, username, password),
    									createHeadersToLogin(MediaType.APPLICATION_FORM_URLENCODED, basicAuthusername, basicAuthpassword));
    									
    	ResponseEntity<LoginResponseDto> response = restTemplate.exchange(uri, HttpMethod.POST, entity, LoginResponseDto.class);
    	return new AccessTokenDto(response.getBody().getAccess_token());
    
    	
    }
    
	@Override
	public CreateSignDto postCreateSign(ContratRequestBody contrat) {
		putNameEmailAndPhoneOnXml(contrat);
		
		String uri = urlBase+urlCreate;
    	
    	RestTemplate restTemplate = new RestTemplate();
    	
    	HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(
    											createBodyToCreateSign(new FileSystemResource(xmlUrl), new FileSystemResource(pdfUrl), hash),
    											createHeadersToCreateSign(MediaType.MULTIPART_FORM_DATA, basicAuthusername, basicAuthpassword));
    			
    	ResponseEntity<CreateSignDto> response = restTemplate.exchange(uri, HttpMethod.POST, entity, CreateSignDto.class);
    	
    	
    	Contrat contrato = new Contrat(response.getBody().getData().getRequisitionId(), response.getBody().getData().getTicket());
    	contratPersistancePort.saveContrat(contrato);
    	//System.out.println(contrato);
		return response.getBody();
    	
		
	}

	private HttpHeaders createHeadersToLogin(
						MediaType mediaType, 
						String basicAuthUsername, 
						String basicAurhPassword) {
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.setBasicAuth(basicAuthUsername, basicAurhPassword);

		
		return headers;
	}
	
	private HttpHeaders createHeadersToCreateSign(MediaType mediaType, 
							String basicAuthUsername, 
							String basicAurhPassword) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.setBasicAuth(basicAuthUsername, basicAurhPassword);
		headers.setBearerAuth(LoginWithFad().getAccess_token());

		
		return headers;
	}
	
	private MultiValueMap<String, Object> createBodyToLogin(String grantType, String username, String password) {
		MultiValueMap<String, Object> bodyParamMap = new LinkedMultiValueMap<String, Object>();
    	bodyParamMap.add("grant_type", grantType);
    	bodyParamMap.add("username", username);
    	bodyParamMap.add("password",password);
    	
    	return bodyParamMap;
	}
	
	private  MultiValueMap<String, Object> createBodyToCreateSign(FileSystemResource xml, FileSystemResource pdf, String hash) {
		MultiValueMap<String, Object> bodyParamMap = new LinkedMultiValueMap<String, Object>();
		bodyParamMap.add("xml", xml);
    	bodyParamMap.add("pdf", pdf);
    	bodyParamMap.add("hash", hash);
    	
    	return bodyParamMap;
	}
	
	private  ChangeXmlDto putNameEmailAndPhoneOnXml(ContratRequestBody contrat) {
	        try {
	            
	            // 1. Cree un objeto Archivo y asigne el archivo XML
	            File file = new File(xmlUrl);
	            // 2. Cree un objeto DocumentBuilderFactory para crear un objeto DocumentBuilder
	            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            // 3. Cree un objeto DocumentBuilder para convertir archivos XML en objetos
	            // Document
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            // 4. Cree un objeto de documento y analice el archivo XML
	            Document document = documentBuilder.parse(file); // Modificar el primer alumno

	            Node signerName = document.getElementsByTagName("signerName").item(0);
	            Node mail = document.getElementsByTagName("mail").item(0);
	            Node phone = document.getElementsByTagName("phone").item(0);
	            Node phone2 = document.getElementsByTagName("signDevicePhone").item(0);

	            Element signerNameElement = (Element) signerName;
	            Element mailElement = (Element) mail;
	            Element phoneElement = (Element) phone;
	            Element phone2Element = (Element) phone2;
	            // 8. Establecer valores para el nodo
	            signerNameElement.setTextContent(contrat.getName());
	            mailElement.setTextContent(contrat.getEmail());
	            phoneElement.setTextContent(contrat.getPhone());
	            phone2Element.setTextContent(contrat.getPhone());
	            // 9. Cree un objeto TransformerFactory
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            // 10. Crea un objeto Transformer
	            Transformer transformer = transformerFactory.newTransformer();
	            // 11. Cree un objeto DOMSource
	            DOMSource domSource = new DOMSource(document);
	            // 12. Crear objeto StreamResult
	            StreamResult reStreamResult = new StreamResult(file);
	            transformer.transform(domSource, reStreamResult);

	            // Resultados de la prueba de salida
	            StreamResult consoleResult = new StreamResult(System.out);
	            transformer.transform(domSource, consoleResult);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new ChangeXmlDto(contrat.getName(), contrat.getEmail(), contrat.getPhone());
	    }

	@Override
	public long countRegisters() {
		return contratPersistancePort.countRegisters();
	}

	

	
}

