package acme.features.levelb.administrator.adBanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Administrator;
import acme.client.services.AbstractService;
import acme.entities.levelb.AdBanner;
import acme.entities.levelb.AdBannerRepository;

@Service
public class AdministratorAdBannerDeleteService extends AbstractService<Administrator, AdBanner> {

	@Autowired
	private AdBannerRepository repository;

	private AdBanner adBanner;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.adBanner = this.repository.findAdBannerById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.adBanner != null;
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.adBanner, "slogan", "targetUrl", "picture");
	}

	@Override
	public void validate() {
		// Nothing to validate before deleting this entity.
	}

	@Override
	public void execute() {
		this.repository.delete(this.adBanner);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.adBanner, "slogan", "targetUrl", "picture");
	}

}
