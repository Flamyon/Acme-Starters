package acme.features.levelb.fundraiser.projectComponents;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.features.levelb.project.ProjectComponent;
import acme.realms.Fundraiser;

@Controller
public class FundraiserProjectComponentsController extends AbstractController<Fundraiser, ProjectComponent> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", FundraiserProjectComponentsListService.class);
		super.addBasicCommand("show", FundraiserProjectComponentsShowService.class);
	}

}