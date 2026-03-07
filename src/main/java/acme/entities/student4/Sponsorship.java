
package acme.entities.student4;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidHeader;
import acme.constraints.ValidSponsorship;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidSponsorship
public class Sponsorship extends AbstractEntity {

	//Serialisation version --------------------------------------------------
	private static final long		serialVersionUID	= 1L;

	//Attributes -------------------------------------------------------------

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String					ticker;

	@Mandatory
	@ValidHeader
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

	// Derived attributes -----------------------------------------------------

	@Transient
	private SponsorshipRepository	sponsorshipRepository;

	/*
	 * totalMoney: - The total money of a
	 * sponsorship is the sum of money in
	 * the corresponding donations. Only
	 * Euros are accepted.
	 */


	@Transient
	public Money getTotalMoney() {
		Money totalMoney = new Money();
		totalMoney.setCurrency("EUR");

		Double res;
		res = this.sponsorshipRepository.calculateTotalMoney(this.getId());

		if (res == null)
			totalMoney.setAmount(0.0);
		else
			totalMoney.setAmount(res);
		return totalMoney;
	}

	/*
	 * - monthsActive is computed as the
	 * number of months in interval
	 * startMoment/endMoment rounded to the
	 * nearest decimal.
	 */

	@Mandatory
	@ValidNumber
	@Transient
	public Double getMonthsActive() {

		if (this.startMoment == null || this.endMoment == null)
			return null;

		Double months = MomentHelper.computeDifference(this.startMoment, this.endMoment, ChronoUnit.MONTHS);

		return Math.round(months * 100.0) / 100.0;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Sponsor sponsor;

}
