
package acme.features.student2.any.campaign;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.Campaign;

@Transient
@Repository
public interface CampaignRepository extends AbstractRepository {

	@Query("Select sum(m.effort) from Milestone m where m.campaign.id = :campaignId")
	Double findEffortByCampaignId(int campaignId);

	@Query("select count(m) from Milestone m where m.campaign.id = :id")
	Long countMilestones(int id);

	@Query("select c from Campaign c where c.ticker = :ticker")
	Campaign findByTicker(String ticker);

	@Query("select c from Campaign c where c.draftMode = false")
	Collection<Campaign> findPublishedCampaigns();

	@Query("select c from Campaign c where c.id = :id")
	Campaign findCampaignById(int id);

}
