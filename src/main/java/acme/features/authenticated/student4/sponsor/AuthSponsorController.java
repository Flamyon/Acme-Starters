
package acme.features.authenticated.student4.sponsor;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.realms.Sponsor;

@Controller
public class AuthSponsorController extends AbstractController<Sponsor, Sponsor> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AuthSponsorListService.class);
		super.addBasicCommand("show", AuthSponsorShowService.class);
	}
}
