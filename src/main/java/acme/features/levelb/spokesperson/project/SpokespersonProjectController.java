package acme.features.levelb.spokesperson.project;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.levelb.Project;
import acme.entities.student2.Spokesperson;

@Controller
public class SpokespersonProjectController extends AbstractController<Spokesperson, Project> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", SpokespersonProjectListService.class);
		super.addBasicCommand("show", SpokespersonProjectShowService.class);
	}

}
