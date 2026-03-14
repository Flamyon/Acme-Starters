
package acme.features.student2.any.milestone;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.student2.Milestone;

@Controller
public class MilestoneController extends AbstractController<Any, Milestone> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", MilestoneListService.class);
		super.addBasicCommand("show", MilestoneShowService.class);
	}
}
