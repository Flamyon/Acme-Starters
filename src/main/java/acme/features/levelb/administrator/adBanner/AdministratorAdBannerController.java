package acme.features.levelb.administrator.adBanner;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractController;
import acme.entities.levelb.AdBanner;

@Controller
public class AdministratorAdBannerController extends AbstractController<Administrator, AdBanner> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AdministratorAdBannerListService.class);
		super.addBasicCommand("show", AdministratorAdBannerShowService.class);
		super.addBasicCommand("create", AdministratorAdBannerCreateService.class);
		super.addBasicCommand("update", AdministratorAdBannerUpdateService.class);
		super.addBasicCommand("delete", AdministratorAdBannerDeleteService.class);
	}

}
