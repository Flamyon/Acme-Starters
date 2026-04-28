
package acme.features.student4.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.student4.Donation;
import acme.entities.student4.Sponsorship;
import acme.entities.student4.SponsorshipRepository;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

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

		// Only the owner can delete, and only while in draftMode
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
		// No extra validations needed for delete
	}

	@Override
	public void execute() {
		Collection<Donation> donations;

		if (this.sponsorship.getId() == 0)
			return;

		// Cascade-delete all linked donations first
		donations = this.repository.findDonationsBySponsorshipId(this.sponsorship.getId());
		this.repository.deleteAll(donations);
		this.repository.delete(this.sponsorship);
	}

	@Override
	public void unbind() {

		Tuple tuple;

		tuple = super.unbindObject(this.sponsorship, //
			"ticker", "name", "description", "startMoment", "endMoment", "moreInfo", //
			"draftMode", "monthsActive", "totalMoney");
		tuple.put("id", this.sponsorship.getId() != 0 ? this.sponsorship.getId() : 0);

	}
}
