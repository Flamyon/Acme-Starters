
package acme.features.student4.sponsor.sponsorship;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.student4.Donation;
import acme.entities.student4.Sponsorship;
import acme.entities.student4.SponsorshipRepository;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorshipRepository	repository;

	private Sponsorship				sponsorship;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		Integer id;

		id = super.getRequest().getData("id", Integer.class, null);
		if (id == null || id.intValue() == 0)
			this.sponsorship = super.newObject(Sponsorship.class);
		else {
			this.sponsorship = this.repository.findSponsorshipById(id.intValue());
			if (this.sponsorship == null)
				this.sponsorship = super.newObject(Sponsorship.class);
		}
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.sponsorship != null && //
			this.sponsorship.getId() != 0 && //
			Boolean.TRUE.equals(this.sponsorship.getDraftMode()) && //
			this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		if (this.sponsorship.getId() == 0)
			return;

		super.validateObject(this.sponsorship);
		{
			Collection<Donation> donations;
			boolean hasDonations;

			donations = this.repository.findDonationsBySponsorshipId(this.sponsorship.getId());
			hasDonations = donations != null && !donations.isEmpty();
			super.state(hasDonations, "totalMoney", "acme.validation.sponsorship.donations.error");
		}

		{
			Date start;
			Date end;
			boolean validInterval;

			start = this.sponsorship.getStartMoment();
			end = this.sponsorship.getEndMoment();
			validInterval = start != null && end != null && MomentHelper.isAfter(end, start);
			super.state(validInterval, "startMoment", "acme.validation.sponsorship.dates.error");
		}

		{
			Date now;
			Date start;
			Date end;
			boolean startInFuture;
			boolean endInFuture;

			now = MomentHelper.getCurrentMoment();
			start = this.sponsorship.getStartMoment();
			end = this.sponsorship.getEndMoment();

			startInFuture = start != null && MomentHelper.isAfter(start, now);
			super.state(startInFuture, "startMoment", "acme.validation.sponsorship.startMoment.future");

			endInFuture = end != null && MomentHelper.isAfter(end, now);
			super.state(endInFuture, "endMoment", "acme.validation.sponsorship.endMoment.future");
		}
	}

	@Override
	public void execute() {
		if (this.sponsorship.getId() == 0)
			return;

		this.sponsorship.setDraftMode(false);
		this.repository.save(this.sponsorship);
	}

	@Override
	public void unbind() {

		Tuple tuple;

		tuple = super.unbindObject(this.sponsorship, //
			"ticker", "name", "description", "startMoment", "endMoment", "moreInfo");

		tuple.put("id", this.sponsorship.getId() != 0 ? this.sponsorship.getId() : 0);
		tuple.put("draftMode", this.sponsorship.getDraftMode());
		tuple.put("monthsActive", this.sponsorship.getMonthsActive());
		tuple.put("totalMoney", this.sponsorship.getTotalMoney());

	}
}
