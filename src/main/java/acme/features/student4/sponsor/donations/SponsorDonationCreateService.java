
package acme.features.student4.sponsor.donations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student4.Donation;
import acme.entities.student4.DonationKind;
import acme.entities.student4.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorDonationCreateService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository	repository;

	private Donation					donation;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findSponsorshipById(sponsorshipId);

		this.donation = super.newObject(Donation.class);
		this.donation.setSponsorship(sponsorship);
	}

	@Override
	public void authorise() {
		boolean status;

		// Parent sponsorship must exist, be in draft, and belong to this sponsor
		status = this.donation.getSponsorship() != null && //
			this.donation.getSponsorship().getDraftMode() && //
			this.donation.getSponsorship().getSponsor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.donation, "name", "notes", "money", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.donation);
	}

	@Override
	public void execute() {
		this.repository.save(this.donation);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(DonationKind.class, this.donation.getKind());

		tuple = super.unbindObject(this.donation, "name", "notes", "money", "kind");
		tuple.put("sponsorshipId", super.getRequest().getData("sponsorshipId", int.class));
		tuple.put("draftMode", this.donation.getSponsorship().getDraftMode());
		tuple.put("kinds", choices);

	}
}
