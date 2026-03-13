
package acme.features.student5.any.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student5.AuditSection;

@Service
public class AnyAuditSectionShowService extends AbstractService<Any, AuditSection> {

	@Autowired
	private AnyAuditSectionRepository	repository;

	private AuditSection				auditSection;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.auditSection = this.repository.findAuditSectionById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.auditSection != null && !this.auditSection.getAuditReport().getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.auditSection, "name", "notes", "hours", "kind");
	}
}
