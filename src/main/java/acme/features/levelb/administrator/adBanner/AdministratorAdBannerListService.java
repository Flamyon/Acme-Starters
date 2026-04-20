package acme.features.levelb.administrator.adBanner;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Administrator;
import acme.client.services.AbstractService;
import acme.entities.levelb.AdBanner;
import acme.entities.levelb.AdBannerRepository;

@Service
public class AdministratorAdBannerListService extends AbstractService<Administrator, AdBanner> {

	@Autowired
	private AdBannerRepository repository;

	private Collection<AdBanner> adBanners;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		this.adBanners = this.repository.findAllAdBanners();
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.adBanners, "slogan", "targetUrl", "picture");
	}

}
