package acme.features.student1.inventor.invention;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;

@Service
public class InventorInventionListService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository repo;

	private Collection<Invention> inventionCollection;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		int ownerRealmId;

		ownerRealmId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.inventionCollection = this.repo.findInventionsByInventorId(ownerRealmId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.inventionCollection, "ticker", "name", "startMoment");
	}
}
