package mx.com.naat.examen.infrastructure.entity;

import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contrat {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "REQUISITION_ID")
	private UUID requisitionId;
	
	@Column(name = "TICKET")
	private String ticket;
	
	
	public Contrat(UUID requisitionId, String ticket) {
		this.requisitionId = requisitionId;
		this.ticket = ticket;
	}
	
}
