
package acme.features.authenticated.student4.sponsorship;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.student4.Sponsorship;

@Controller
public class AuthSponsorshipController extends AbstractController<Any, Sponsorship> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AuthSponsorshipListService.class);
		super.addBasicCommand("show", AuthSponsorshipShowService.class);
	}
}
