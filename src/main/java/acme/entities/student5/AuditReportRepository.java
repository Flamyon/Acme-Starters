
package acme.entities.student5;

import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Transient
@Repository
public interface AuditReportRepository extends AbstractRepository {

	@Query("select sum(a.hours) from AuditSection a where a.auditReport.id = :id")
	Double findHoursByAuditReportId(int id);

	@Query("select count(a) from AuditSection a where a.auditReport.id = :id")
	Long countAuditSections(int id);

}
