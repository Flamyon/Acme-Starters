
package acme.features.student4.sponsor.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.student4.Sponsorship;
import acme.entities.student4.SponsorshipRepository;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipShowService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

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

		// Owner always sees it; others only see published ones
		status = this.sponsorship != null && this.sponsorship.getId() != 0 && //
			(this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().isPrincipal() || Boolean.FALSE.equals(this.sponsorship.getDraftMode()));

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		boolean isOwner;
		Tuple tuple;

		tuple = super.unbindObject(this.sponsorship, //
			"ticker", "name", "description", "startMoment", "endMoment", "moreInfo", //
			"draftMode", "monthsActive", "totalMoney");
		tuple.put("id", this.sponsorship.getId() != 0 ? this.sponsorship.getId() : 0);
		isOwner = this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().isPrincipal();
		tuple.put("isOwner", isOwner);
		tuple.put("canAssociateProject", isOwner && Boolean.FALSE.equals(this.sponsorship.getDraftMode()));
	}
}
