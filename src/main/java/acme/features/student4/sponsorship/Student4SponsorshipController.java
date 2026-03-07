
package acme.features.student4.sponsorship;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.student4.Sponsorship;

@Controller
public class Student4SponsorshipController extends AbstractController<Any, Sponsorship> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", Student4SponsorshipListService.class);
		super.addBasicCommand("show", Student4SponsorshipShowService.class);
	}
}
