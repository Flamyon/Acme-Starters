
package acme.features.student2.any.spokesperson;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.student2.Spokesperson;

@Controller
public class SpokespersonController extends AbstractController<Any, Spokesperson> {

	// Service registration ---------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		// Registramos los dos servicios que hemos adaptado antes
		super.addBasicCommand("list", SpokespersonListService.class);
		super.addBasicCommand("show", SpokespersonShowService.class);
	}
}
