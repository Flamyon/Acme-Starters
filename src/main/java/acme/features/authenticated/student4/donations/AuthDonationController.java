
package acme.features.authenticated.student4.donations;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.student4.Donation;
import acme.realms.Sponsor;

@Controller
public class AuthDonationController extends AbstractController<Sponsor, Donation> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AuthDonationListService.class);
		super.addBasicCommand("show", AuthDonationShowService.class);
	}
}
