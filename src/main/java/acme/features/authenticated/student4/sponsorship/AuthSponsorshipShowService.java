
package acme.features.authenticated.student4.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student4.Sponsorship;
import acme.entities.student4.SponsorshipRepository;

@Service
public class AuthSponsorshipShowService extends AbstractService<Any, Sponsorship> {

	// Internal state ---------------------------------------------------------

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

		status = this.sponsorship != null && !this.sponsorship.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		Money totalMoney;
		Double months;

		tuple = super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

		tuple.put("sponsorId", this.sponsorship.getSponsor().getId());

		// totalMoney calculado desde el repository
		Double res = this.repository.calculateTotalMoney(this.sponsorship.getId());
		totalMoney = new Money();
		totalMoney.setCurrency("EUR");
		totalMoney.setAmount(res == null ? 0.0 : res);
		tuple.put("totalMoney", totalMoney);

		// monthsActive calculado aquí
		months = this.sponsorship.getMonthsActive();
		tuple.put("monthsActive", months);
	}
}
