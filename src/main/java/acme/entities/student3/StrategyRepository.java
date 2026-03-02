
package acme.entities.student3;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface StrategyRepository extends AbstractRepository {

	@Query("SELECT SUM(t.expectedPercentage) FROM Tactic t WHERE t.strategy.id = :id")
	Double expectedPercentage(@Param("id") int strategyId);

	@Query("SELECT COUNT(t) FROM Tactic t WHERE t.strategy.id = :id")
	Long countTacticsByStrategyId(@Param("id") int id);

	@Query("SELECT COUNT(s) FROM Strategy s WHERE s.ticker = :ticker")
	Long countByTicker(@Param("ticker") String ticker);

	@Query("SELECT COUNT(s) FROM Strategy s WHERE s.ticker = :ticker AND s.id != :id")
	Long countByTickerAndNotId(@Param("ticker") String ticker, @Param("id") int id);
}
