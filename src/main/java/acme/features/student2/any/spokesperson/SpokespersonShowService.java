
package acme.features.student2.any.spokesperson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonShowService extends AbstractService<Any, Spokesperson> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SpokespersonRepository	repository;

	private Spokesperson			spokesperson;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.spokesperson = this.repository.findSpokespersonById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		// Solo autorizamos si el portavoz existe en la base de datos
		status = this.spokesperson != null;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		// Ajustamos los atributos a los propios de Spokesperson
		super.unbindObject(this.spokesperson, "cv", "achievements", "licensed", "identity.name", "identity.surname", "identity.email");
	}
}
