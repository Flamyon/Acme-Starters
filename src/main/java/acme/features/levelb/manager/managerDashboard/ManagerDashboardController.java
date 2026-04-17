package acme.features.levelb.manager.managerDashboard;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@Controller
public class ManagerDashboardController extends AbstractController<Manager, ManagerDashboard> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("show", ManagerDashboardShowService.class);
	}

}
