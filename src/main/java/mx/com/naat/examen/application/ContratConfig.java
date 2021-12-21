package mx.com.naat.examen.application;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mx.com.naat.examen.domain.api.ContratServicePort;
import mx.com.naat.examen.domain.service.ContratServiceImpl;
import mx.com.naat.examen.domain.spi.ContratPersistancePort;
import mx.com.naat.examen.infrastructure.adapter.ContratJpaAdapter;

@Configuration
public class ContratConfig {

	@Bean
	public ContratPersistancePort contratPersistance() {
		return new ContratJpaAdapter();
	}

	@Bean
	public ContratServicePort contratServicePort() {
		return new ContratServiceImpl(contratPersistance());
	}

	
	/*
	 * @Bean public WebMvcConfigurer corsConfigurer() { return new
	 * WebMvcConfigurer() {
	 * 
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/**") .allowedOrigins("*") .allowedMethods("GET",
	 * "PUT","POST", "PATCH", "DELETE", "OPTIONS"); } }; }
	 */
	
}