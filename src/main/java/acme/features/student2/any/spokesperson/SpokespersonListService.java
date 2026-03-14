
package acme.features.student2.any.spokesperson;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonListService extends AbstractService<Any, Spokesperson> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SpokespersonRepository		repository;

	private Collection<Spokesperson>	spokespersons;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		this.spokespersons = this.repository.findAllSpokespersons();
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.spokespersons, "cv", "achievements", "licensed", "identity.name", "identity.surname", "identity.email");
	}
}
