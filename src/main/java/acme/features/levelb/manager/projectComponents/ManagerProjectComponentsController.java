package acme.features.levelb.manager.projectComponents;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.features.levelb.project.ProjectComponent;
import acme.realms.Manager;

@Controller
public class ManagerProjectComponentsController extends AbstractController<Manager, ProjectComponent> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", ManagerProjectComponentsListService.class);
		super.addBasicCommand("show", ManagerProjectComponentsShowService.class);
	}

}