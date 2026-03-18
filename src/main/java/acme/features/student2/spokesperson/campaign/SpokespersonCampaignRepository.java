
package acme.features.student2.spokesperson.campaign;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.Campaign;
import acme.entities.student2.Milestone;

@Repository
public interface SpokespersonCampaignRepository extends AbstractRepository {

	@Query("select c from Campaign c where c.ticker = :ticker")
	Campaign findCampaignByTicker(String ticker);
	@Query("select c from Campaign c where c.id = :id")
	Campaign findCampaignById(int id);
	@Query("select c from Campaign c where c.spokesperson.id = :id")
	Collection<Campaign> findCampaignsBySpokespersonId(int id);
	@Query("select m from Milestone m where m.campaign.id = :id")
	Collection<Milestone> findMilestonesByCampaignId(int id);
	@Query("select count(m) from Milestone m where m.campaign.id = :id")
	Integer countMilestonesFromCampaignId(int id);
}
