package mx.com.naat.examen.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignersDtoToSend {
	 private String signerName;
     private String mail;
     private String phone;
}
