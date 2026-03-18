
package acme.features.authenticated.student4.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.realms.Sponsor;

@Service
public class AuthSponsorShowService extends AbstractService<Sponsor, Sponsor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthSponsorRepository	repository;

	private Sponsor					sponsor;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsor = this.repository.findSponsorById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.sponsor != null;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.sponsor, "address", "im", "gold", "identity.name", "identity.surname", "identity.email");
	}
}
