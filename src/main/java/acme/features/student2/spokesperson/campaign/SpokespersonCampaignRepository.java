
package acme.features.student2.spokesperson.campaign;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.levelb.Project;
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

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select distinct p from Project p join p.members pm where p.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = acme.entities.levelb.MemberRole.SPOKESPERSON order by p.title asc")
	Collection<Project> findAvailableProjectsByMemberUserAccountId(int userAccountId);

	@Query("select count(pm) from ProjectMember pm where pm.project.id = :projectId and pm.project.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = acme.entities.levelb.MemberRole.SPOKESPERSON")
	Long countDraftMembershipByProjectAndUserAccountId(int projectId, int userAccountId);
}
