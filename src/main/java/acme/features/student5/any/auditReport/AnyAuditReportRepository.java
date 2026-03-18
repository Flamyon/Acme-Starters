
package acme.features.student5.any.auditReport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student5.AuditReport;

@Repository
public interface AnyAuditReportRepository extends AbstractRepository {

	@Query("select a from AuditReport a where draftMode = false")
	List<AuditReport> findPublishedAuditReports();

	@Query("select a from AuditReport a where a.id = :id")
	AuditReport findAuditReportById(int id);
}
