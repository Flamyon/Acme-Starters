package acme.features.levelb.administrator.adbanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Administrator;
import acme.client.services.AbstractService;
import acme.entities.levelb.AdBanner;
import acme.entities.levelb.AdBannerRepository;

@Service
public class AdministratorAdBannerCreateService extends AbstractService<Administrator, AdBanner> {

	@Autowired
	private AdBannerRepository repository;

	private AdBanner adBanner;


	@Override
	public void load() {
		this.adBanner = super.newObject(AdBanner.class);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void bind() {
		super.bindObject(this.adBanner, "slogan", "targetUrl", "picture");
	}

	@Override
	public void validate() {
		super.validateObject(this.adBanner);
	}

	@Override
	public void execute() {
		this.repository.save(this.adBanner);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.adBanner, "slogan", "targetUrl", "picture");
	}

}
