
package acme.entities.student1;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidInvention
public class Invention extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String					ticker;

	@Mandatory
	@ValidText
	@Column
	private String					name;

	@Mandatory
	@ValidText
	@Column
	private String					description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					endMoment;

	@Optional
	@ValidUrl
	@Column
	private String					moreInfo;

	@Mandatory
	@Valid
	@Column
	private Boolean					draftMode;

	@Transient
	@Autowired
	private InventionCostRepository	partRepository;


	@Transient
	public Money getCost(final int inventionId) {
		Double amount;
		Money result;

		amount = this.partRepository.findCostByInventionId(inventionId);
		result = new Money();
		result.setCurrency("EUR");
		result.setAmount(amount == null ? 0.0 : amount);

		return result;
	}

	@Transient
	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.0;

		long diffInMillies = this.endMoment.getTime() - this.startMoment.getTime();

		double days = diffInMillies / (1000.0 * 60 * 60 * 24);

		double months = days / 30.0;

		return Math.round(months * 10.0) / 10.0;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Inventor inventor;

}
