package acme.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.client.helpers.LoggerHelper;
import acme.entities.levelb.AdBanner;
import acme.entities.levelb.AdBannerRepository;

@ControllerAdvice
public class AdBannerAdvisor {

	@Autowired
	private AdBannerRepository repository;


	@ModelAttribute("adBanner")
	public AdBanner getAdBanner() {
		AdBanner result;

		try {
			result = this.repository.findRandomAdBanner();
		} catch (final Throwable oops) {
			LoggerHelper.error(oops.getLocalizedMessage());
			result = null;
		}

		return result;
	}

}
