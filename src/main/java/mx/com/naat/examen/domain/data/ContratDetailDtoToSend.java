package mx.com.naat.examen.domain.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContratDetailDtoToSend {
	private String idRequisitionClient;
    private String contractName;
    private String ownerName;
    private List<SignersDtoToSend> signers;
    private String status;
}