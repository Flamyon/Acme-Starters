
package acme.features.student4.sponsor.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student4.Sponsorship;
import acme.entities.student4.SponsorshipRepository;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorshipRepository	repository;

	private Sponsorship				sponsorship;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findSponsorshipById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		// Only the owner can update, and only while in draftMode
		status = this.sponsorship != null && //
			this.sponsorship.getDraftMode() && //
			this.sponsorship.getSponsor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.sponsorship);
	}

	@Override
	public void execute() {
		this.repository.save(this.sponsorship);
	}

	@Override
	public void unbind() {

		super.unbindObject(this.sponsorship, //
			"ticker", "name", "description", "startMoment", "endMoment", "moreInfo", //
			"draftMode", "monthsActive", "totalMoney");

	}
}
