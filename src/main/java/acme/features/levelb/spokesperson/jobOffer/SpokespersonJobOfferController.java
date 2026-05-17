package acme.features.levelb.spokesperson.jobOffer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.student2.Spokesperson;
import acme.forms.JobOffer;
import acme.features.levelb.spokesperson.project.SpokespersonProjectJobOfferListService;

@Controller
public class SpokespersonJobOfferController extends AbstractController<Spokesperson, JobOffer> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(org.springframework.http.MediaType.TEXT_HTML);
		super.addBasicCommand("list", SpokespersonProjectJobOfferListService.class);
	}

}
