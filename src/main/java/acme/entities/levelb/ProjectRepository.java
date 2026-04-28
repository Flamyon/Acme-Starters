
package acme.entities.levelb;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;
import acme.entities.student2.Campaign;
import acme.entities.student2.Spokesperson;
import acme.entities.student3.Strategy;
import acme.entities.student4.Sponsorship;
import acme.entities.student5.AuditReport;
import acme.realms.Fundraiser;
import acme.realms.Manager;

@Repository
public interface ProjectRepository extends AbstractRepository {

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(@Param("id") int id);

	@Query("select p from Project p left join fetch p.manager where p.id=:id")
	Project findProjectByIdWithDetails(@Param("id") int id);

	@Query("select p from Project p where p.manager.id = :managerId order by p.kickOff desc")
	Collection<Project> findProjectsByManagerId(@Param("managerId") int managerId);

	@Query("select p from Project p where p.draftMode = false order by p.kickOff desc")
	Collection<Project> findPublishedProjects();

	@Query("select distinct p from ProjectMember pm join pm.project p where pm.userAccount.id = :userAccountId and pm.roleKind = :roleKind order by p.kickOff desc")
	Collection<Project> findProjectsByRoleMemberUserAccountId(@Param("userAccountId") int userAccountId, @Param("roleKind") MemberRole roleKind);

	@Query("select distinct p from ProjectMember pm join pm.project p where p.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = :roleKind order by p.title asc")
	Collection<Project> findDraftProjectsByRoleMemberUserAccountId(@Param("userAccountId") int userAccountId, @Param("roleKind") MemberRole roleKind);

	@Query("select count(pm) from ProjectMember pm where pm.project.id = :projectId and pm.userAccount.id = :userAccountId and pm.roleKind = :roleKind")
	Long countProjectMemberByProjectIdAndRoleKindAndUserAccountId(@Param("projectId") int projectId, @Param("userAccountId") int userAccountId, @Param("roleKind") MemberRole roleKind);

	@Query("select count(pm) from ProjectMember pm where pm.project.id = :projectId and pm.project.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = :roleKind")
	Long countDraftProjectMemberByProjectIdAndRoleKindAndUserAccountId(@Param("projectId") int projectId, @Param("userAccountId") int userAccountId, @Param("roleKind") MemberRole roleKind);

	@Query("select count(pm) from ProjectMember pm where pm.project.id = :projectId and pm.userAccount.id = :userAccountId and pm.roleKind = :roleKind")
	Long countProjectMemberByProjectIdAndUserAccountIdAndRoleKind(@Param("projectId") int projectId, @Param("userAccountId") int userAccountId, @Param("roleKind") MemberRole roleKind);

	@Query("select pm from ProjectMember pm join fetch pm.userAccount where pm.project.id = :projectId")
	Collection<ProjectMember> findProjectMembersByProjectId(@Param("projectId") int projectId);

	@Query("select pm from ProjectMember pm join fetch pm.project p join fetch p.manager join fetch pm.userAccount where pm.id = :id")
	ProjectMember findProjectMemberById(@Param("id") int id);

	@Query("select count(pm) from ProjectMember pm where pm.project.id = :projectId")
	Long countProjectMembersByProjectId(@Param("projectId") int projectId);

	@Query("select i from Inventor i where not exists (select pm from ProjectMember pm where pm.project.id = :projectId and pm.userAccount.id = i.userAccount.id and pm.roleKind = :roleKind)")
	Collection<Inventor> findNomineeInventorsByProjectId(@Param("projectId") int projectId, @Param("roleKind") MemberRole roleKind);

	@Query("select s from Spokesperson s where not exists (select pm from ProjectMember pm where pm.project.id = :projectId and pm.userAccount.id = s.userAccount.id and pm.roleKind = :roleKind)")
	Collection<Spokesperson> findNomineeSpokespersonsByProjectId(@Param("projectId") int projectId, @Param("roleKind") MemberRole roleKind);

	@Query("select f from Fundraiser f where not exists (select pm from ProjectMember pm where pm.project.id = :projectId and pm.userAccount.id = f.userAccount.id and pm.roleKind = :roleKind)")
	Collection<Fundraiser> findNomineeFundraisersByProjectId(@Param("projectId") int projectId, @Param("roleKind") MemberRole roleKind);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(@Param("id") int id);

	@Query("select i from Inventor i where i.userAccount.id = :userAccountId")
	Inventor findInventorByUserAccountId(@Param("userAccountId") int userAccountId);

	@Query("select s from Spokesperson s where s.userAccount.id = :userAccountId")
	Spokesperson findSpokespersonByUserAccountId(@Param("userAccountId") int userAccountId);

	@Query("select f from Fundraiser f where f.userAccount.id = :userAccountId")
	Fundraiser findFundraiserByUserAccountId(@Param("userAccountId") int userAccountId);

	@Query("select count(i) from Inventor i where i.userAccount.id = :userAccountId")
	Long countInventorRolesByUserAccountId(@Param("userAccountId") int userAccountId);

	@Query("select count(s) from Spokesperson s where s.userAccount.id = :userAccountId")
	Long countSpokespersonRolesByUserAccountId(@Param("userAccountId") int userAccountId);

	@Query("select count(f) from Fundraiser f where f.userAccount.id = :userAccountId")
	Long countFundraiserRolesByUserAccountId(@Param("userAccountId") int userAccountId);

	default Long countSupportedRolesByUserAccountId(final int userAccountId) {
		Long inventors;
		Long spokespersons;
		Long fundraisers;

		inventors = this.countInventorRolesByUserAccountId(userAccountId);
		spokespersons = this.countSpokespersonRolesByUserAccountId(userAccountId);
		fundraisers = this.countFundraiserRolesByUserAccountId(userAccountId);

		return (inventors == null ? 0L : inventors) + (spokespersons == null ? 0L : spokespersons) + (fundraisers == null ? 0L : fundraisers);
	}

	@Query("select i from Invention i where i.project.id = :projectId")
	Collection<Invention> findInventionsByProjectId(@Param("projectId") int projectId);

	@Query("select i from Invention i where i.project.id = :projectId and i.inventor.userAccount.id = :userAccountId")
	Collection<Invention> findInventionsByProjectIdAndInventorUserAccountId(@Param("projectId") int projectId, @Param("userAccountId") int userAccountId);

	@Query("select c from Campaign c where c.project.id = :projectId")
	Collection<Campaign> findCampaignsByProjectId(@Param("projectId") int projectId);

	@Query("select c from Campaign c where c.project.id = :projectId and c.spokesperson.userAccount.id = :userAccountId")
	Collection<Campaign> findCampaignsByProjectIdAndSpokespersonUserAccountId(@Param("projectId") int projectId, @Param("userAccountId") int userAccountId);

	@Query("select s from Strategy s where s.project.id = :projectId")
	Collection<Strategy> findStrategiesByProjectId(@Param("projectId") int projectId);

	@Query("select sp from Sponsorship sp where sp.project.id = :projectId")
	Collection<Sponsorship> findSponsorshipsByProjectId(@Param("projectId") int projectId);

	@Query("select ar from AuditReport ar where ar.project.id = :projectId")
	Collection<AuditReport> findAuditReportsByProjectId(@Param("projectId") int projectId);

	@Query("select s from Strategy s where s.project.id = :projectId and s.fundraiser.userAccount.id = :userAccountId")
	Collection<Strategy> findStrategiesByProjectIdAndFundraiserUserAccountId(@Param("projectId") int projectId, @Param("userAccountId") int userAccountId);

	@Query("select i from Invention i where i.id = :id")
	Invention findInventionById(@Param("id") int id);

	@Query("select c from Campaign c where c.id = :id")
	Campaign findCampaignById(@Param("id") int id);

	@Query("select s from Strategy s where s.id = :id")
	Strategy findStrategyById(@Param("id") int id);

	@Query("select count(i) from Invention i where i.project.id = :id")
	Long countInventionsByProjectId(@Param("id") int id);

	@Query("select count(c) from Campaign c where c.project.id = :id")
	Long countCampaignsByProjectId(@Param("id") int id);

	@Query("select count(s) from Strategy s where s.project.id = :id")
	Long countStrategiesByProjectId(@Param("id") int id);

	@Query("select count(p) from Part p where p.invention.id = :inventionId")
	Long countPartsByInventionId(@Param("inventionId") int inventionId);

	@Query("select count(m) from Milestone m where m.campaign.id = :campaignId")
	Long countMilestonesByCampaignId(@Param("campaignId") int campaignId);

	@Query("select count(t) from Tactic t where t.strategy.id = :strategyId")
	Long countTacticsByStrategyId(@Param("strategyId") int strategyId);

}
