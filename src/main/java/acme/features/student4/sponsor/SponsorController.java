
package acme.features.student4.sponsor;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.realms.Sponsor;

@Controller
public class SponsorController extends AbstractController<Any, Sponsor> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", SponsorListService.class);
		super.addBasicCommand("show", SponsorShowService.class);
	}
}
