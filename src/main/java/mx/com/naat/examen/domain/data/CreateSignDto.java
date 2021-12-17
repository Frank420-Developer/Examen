package mx.com.naat.examen.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSignDto {
    private boolean success;
    private String error;
    private String code;
    private CreateSignDataDto data;
}