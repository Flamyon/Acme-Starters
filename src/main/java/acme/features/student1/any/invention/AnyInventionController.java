package acme.features.student1.any.invention;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.student1.Invention;

@Controller
public class AnyInventionController extends AbstractController<Any, Invention> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AnyInventionListService.class);
		super.addBasicCommand("show", AnyInventionShowService.class);
	}
}
