
package acme.features.student5.auditor.auditReport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student5.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportListService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository	repo;

	private Collection<AuditReport>			auditReportCollection;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		int ownerRealmId;

		ownerRealmId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.auditReportCollection = this.repo.findAuditReportByAuditorId(ownerRealmId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.auditReportCollection, "ticker", "name", "startMoment", "endMoment");
	}

}
