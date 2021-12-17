package mx.com.naat.examen.domain.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContratRequestBody implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//@NotNull
	private String name;
	
	//@NotNull
	private String email;

	//@NotNull
	private String phone;
}