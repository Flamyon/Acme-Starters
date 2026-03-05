
package acme.entities.student2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Transient
@Repository
public interface CampaignRepository extends AbstractRepository {

	@Query("Select sum(m.effort) from Milestone m where m.campaign.id = :campaignId")
	Double findEffortByCampaignId(int campaignId);

	@Query("select count(m) from Milestone m where m.campaign.id = :id")
	Long countMilestones(int id);

	@Query("select c from Campaign c where c.ticker = :ticker")
	Campaign findByTicker(String ticker);

}
