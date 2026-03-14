
package acme.features.student2.any.milestone;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.Milestone;

@Transient
@Repository
public interface MilestoneRepository extends AbstractRepository {

	@Query("select m from Milestone m where m.campaign.id = :campaignId")
	Collection<Milestone> findMilestonesByCampaignId(int campaignId);

	@Query("select m from Milestone m where m.id = :id")
	Milestone findMilestoneById(int id);

}
