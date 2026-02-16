
package acme.entities.student2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Campaign extends AbstractEntity {

	// --- SerialVersionUID ---

	private static final long	serialVersionUID	= 1L;

	// --- Attributes ---

	@Mandatory
	//@ValidTicker  // Valida el formato del ticker
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	//@ValidText // Equivale a ValidHeader
	@Column
	private String				name;

	@Mandatory
	//@ValidText // Equivale a ValidText
	@Column
	private String				description;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	private Date				startMoment;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	private Date				endMoment;

	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	@Valid
	@Column
	private Boolean				draftMode;


	@Transient
	public Double getMonthsActive() {
		// TODO: Implementar lógica (ej. diferencia entre startMoment y endMoment)
		return 0.0;
	}

	@Transient
	public Double getEffort() {
		// TODO: Implementar lógica de cálculo del esfuerzo
		return 0.0;
	}


	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Spokesperson campaign;

}
