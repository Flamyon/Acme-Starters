
package acme.entities.student5;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	@Query("SELECT COUNT(a) FROM AuditReport a WHERE a.ticker = :ticker")
	Long countByTicker(@Param("ticker") String ticker);

	@Query("SELECT COUNT(a) FROM AuditReport a WHERE a.ticker = :ticker AND a.id != :id")
	Long countByTickerAndNotId(@Param("ticker") String ticker, @Param("id") int id);

	@Query("SELECT COUNT(a) FROM AuditSection a WHERE a.auditReport.id = :id")
	Integer countAuditSectionsByAuditReportId(@Param("id") int id);

	@Query("select a from AuditReport a where a.ticker = :ticker")
	AuditReport findByTicker(String ticker);

}
