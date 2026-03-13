
package acme.features.student5.any.auditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student5.AuditReport;
import acme.realms.Auditor;

@Repository
public interface AnyAuditorRepository extends AbstractRepository {

	@Query("select a from AuditReport a where a.id = :id")
	AuditReport getAuditReportById(int id);

	@Query("select au from Auditor au where au.id = :id")
	Auditor getAuditorById(int id);
}
