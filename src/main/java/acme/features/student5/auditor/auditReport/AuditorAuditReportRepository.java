
package acme.features.student5.auditor.auditReport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student5.AuditReport;
import acme.entities.student5.AuditSection;

@Repository
public interface AuditorAuditReportRepository extends AbstractRepository {

	@Query("select ar from AuditReport ar where ar.ticker = :ticker")
	AuditReport findAuditReportByTicker(String ticker);

	@Query("select ar from AuditReport ar where ar.id = :id")
	AuditReport findAuditReportById(int id);

	@Query("select ar from AuditReport ar where ar.auditor.id = :id")
	Collection<AuditReport> findAuditReportByAuditorId(int id);

	@Query("select ast from AuditSection ast where ast.auditReport.id = :id")
	Collection<AuditSection> findAuditSectionsByAuditReportId(int id);

	@Query("select Count(ast) from AuditSection ast where ast.auditReport.id = :id")
	Integer countAuditSectionsFromAuditReportId(int id);
}
