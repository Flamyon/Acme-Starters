package acme.features.levelb.any.project;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.levelb.Project;

@Controller
public class AnyProjectController extends AbstractController<Any, Project> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AnyProjectListService.class);
		super.addBasicCommand("show", AnyProjectShowService.class);
	}

}
