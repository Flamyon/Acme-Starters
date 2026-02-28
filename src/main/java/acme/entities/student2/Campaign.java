
package acme.entities.student2;

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
@ValidCampaign
public class Campaign extends AbstractEntity {

	@Transient
	@Autowired
	private CampaignRepository	campaignRepository;

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

		long diffInMillies = this.endMoment.getTime() - this.startMoment.getTime();

		double days = diffInMillies / (1000.0 * 60 * 60 * 24);

		double months = days / 30.0;

		return Math.round(months * 10.0) / 10.0;
	}

	@Transient
	public Double getEffort() {
		Double effort;
		effort = this.campaignRepository.findEffortByCampaignId(this.getId());
		return effort;
	}


	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Spokesperson spokeperson;

}
