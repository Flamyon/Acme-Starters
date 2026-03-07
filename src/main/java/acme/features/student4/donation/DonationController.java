
package acme.features.student4.donation;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.student4.Donation;

@Controller
public class DonationController extends AbstractController<Any, Donation> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", DonationListService.class);
		super.addBasicCommand("show", DonationShowService.class);
	}
}
