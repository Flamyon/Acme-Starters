
package acme.features.student2.spokesperson.milestone;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.Campaign;
import acme.entities.student2.Milestone;

@Repository
public interface SpokespersonMilestoneRepository extends AbstractRepository {

	@Query("select m from Milestone m where m.campaign.id = :id")
	Collection<Milestone> findMilestonesByCampaignId(int id);
	@Query("select c from Campaign c where c.id = :id")
	Campaign findCampaignById(int id);
	@Query("select m from Milestone m where m.id = :id")
	Milestone findMilestoneById(int id);

}
