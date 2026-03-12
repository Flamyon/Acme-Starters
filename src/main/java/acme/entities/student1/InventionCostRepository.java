
package acme.entities.student1;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Transient
@Repository
public interface InventionCostRepository extends AbstractRepository {

	@Query("select sum(p.cost.amount) from Part p where p.invention.id = :inventionId and p.cost.currency = 'EUR'")
	Double findCostByInventionId(@Param("inventionId") int inventionId);

	@Query("select count(p) > 0 from Part p where p.invention.id = :inventionId")
	boolean hasAnyPartByInventionId(@Param("inventionId") int inventionId);

	@Query("select c from Invention c where c.ticker = :ticker")
	Invention findByTicker(String ticker);

}

