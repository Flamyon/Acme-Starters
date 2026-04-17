package acme.features.levelb.inventor.projectComponents;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.student1.Inventor;
import acme.features.levelb.project.ProjectComponent;

@Controller
public class InventorProjectComponentsController extends AbstractController<Inventor, ProjectComponent> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", InventorProjectComponentsListService.class);
		super.addBasicCommand("show", InventorProjectComponentsShowService.class);
	}

}