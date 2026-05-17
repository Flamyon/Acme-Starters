package acme.features.levelb.fundraiser.jobOffer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.realms.Fundraiser;
import acme.forms.JobOffer;
import acme.features.levelb.fundraiser.project.FundraiserProjectJobOfferListService;

@Controller
public class FundraiserJobOfferController extends AbstractController<Fundraiser, JobOffer> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(org.springframework.http.MediaType.TEXT_HTML);
		super.addBasicCommand("list", FundraiserProjectJobOfferListService.class);
	}

}
