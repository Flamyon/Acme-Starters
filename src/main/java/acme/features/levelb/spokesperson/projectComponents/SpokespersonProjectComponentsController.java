package acme.features.levelb.spokesperson.projectComponents;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.student2.Spokesperson;
import acme.features.levelb.project.ProjectComponent;

@Controller
public class SpokespersonProjectComponentsController extends AbstractController<Spokesperson, ProjectComponent> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", SpokespersonProjectComponentsListService.class);
		super.addBasicCommand("show", SpokespersonProjectComponentsShowService.class);
	}

}