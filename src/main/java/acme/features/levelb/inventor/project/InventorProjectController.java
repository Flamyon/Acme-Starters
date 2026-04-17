package acme.features.levelb.inventor.project;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.levelb.Project;
import acme.entities.student1.Inventor;

@Controller
public class InventorProjectController extends AbstractController<Inventor, Project> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", InventorProjectListService.class);
		super.addBasicCommand("show", InventorProjectShowService.class);
	}

}
