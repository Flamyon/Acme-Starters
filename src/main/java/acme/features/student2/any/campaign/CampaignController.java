
package acme.features.student2.any.campaign;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.student2.Campaign;

@Controller
public class CampaignController extends AbstractController<Any, Campaign> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", CampaignListService.class);
		super.addBasicCommand("show", CampaignShowService.class);
	}

}
