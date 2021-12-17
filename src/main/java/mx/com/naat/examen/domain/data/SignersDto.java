package mx.com.naat.examen.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignersDto {
    private String signerName;
    private int order;
    private boolean signed;
    private String mail;
    private String phone;
}
