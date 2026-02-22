
package acme.entities.student2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

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
		if (this.startMoment == null || this.endMoment == null)
			return 0.0;

		// 1. Calculamos la diferencia en milisegundos
		long diffInMillies = this.endMoment.getTime() - this.startMoment.getTime();

		// 2. Pasamos a días (1 día = 1000ms * 60s * 60m * 24h)
		double days = diffInMillies / (1000.0 * 60 * 60 * 24);

		// 3. Pasamos a meses (se suele asumir el mes contable de 30 días)
		double months = days / 30.0;

		// 4. Redondeamos al decimal más cercano (rounded to the nearest decimal)
		return Math.round(months * 10.0) / 10.0;
	}

	@Transient
	public Double getEffort() {
		//necesito campaings para calcularlo
		return 0.0;

	}

	@AssertTrue(message = "A published campaign must have at least one milestone")
	public boolean isPublishedWithMilestones() {
		//Necesito campaings para comprobarlo
		return true;
	}

	@AssertTrue(message = "Start moment must be before end moment")
	public boolean isValidTimeInterval() {
		if (this.startMoment != null && this.endMoment != null)
			return this.startMoment.before(this.endMoment);
		return true;
	}


	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Spokesperson spokeperson;

}
