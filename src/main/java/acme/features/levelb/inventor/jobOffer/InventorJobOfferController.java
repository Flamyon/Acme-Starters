package acme.features.levelb.inventor.jobOffer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.student1.Inventor;
import acme.forms.JobOffer;
import acme.features.levelb.inventor.project.InventorProjectJobOfferListService;

@Controller
public class InventorJobOfferController extends AbstractController<Inventor, JobOffer> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(org.springframework.http.MediaType.TEXT_HTML);
		super.addBasicCommand("list", InventorProjectJobOfferListService.class);
	}

}
