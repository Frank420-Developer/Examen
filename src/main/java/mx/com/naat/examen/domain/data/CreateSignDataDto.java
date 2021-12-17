package mx.com.naat.examen.domain.data;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSignDataDto {
	 private UUID requisitionId;
     private String url;
     private String key;
     private String vector;
     private String ticket;
     private AllTickets allTickets;
         
}
