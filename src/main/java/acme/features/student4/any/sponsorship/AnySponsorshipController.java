
package acme.features.student4.any.sponsorship;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.student4.Sponsorship;
import acme.realms.Sponsor;

@Controller
public class AnySponsorshipController extends AbstractController<Sponsor, Sponsorship> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AnySponsorshipListService.class);
		super.addBasicCommand("show", AnySponsorshipShowService.class);
	}
}
