package mx.com.naat.examen.domain.data;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataDto {
    private String idRequisitionClient;
    private String contractName;
    private String cnsf;
    private int currentVersion;
    private String dateContractRequisition;
    private UUID idCompany;
    private String nameCompany;
    private UUID idRequisition;
    private String ownerName;
    private String validity;
    private String acceptanceLegend;
    private String idDocument;
    private List<SignersDto> signers;
    private String status;
    private String extraInfo;
}