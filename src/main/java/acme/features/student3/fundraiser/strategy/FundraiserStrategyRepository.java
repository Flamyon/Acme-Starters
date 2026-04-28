
package acme.features.student3.fundraiser.strategy;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.levelb.Project;
import acme.entities.student3.Strategy;
import acme.entities.student3.Tactic;

@Repository
public interface FundraiserStrategyRepository extends AbstractRepository {

	@Query("select s from Strategy s where s.ticker = :ticker")
	Strategy findStrategyByTicker(String ticker);

	@Query("select s from Strategy s where s.id = :id")
	Strategy findStrategyById(int id);

	@Query("select s from Strategy s where s.fundraiser.id = :id")
	Collection<Strategy> findStrategyByFundraiserId(int id);

	@Query("select t from Tactic t where t.strategy.id = :id")
	Collection<Tactic> findTacticsByStrategyId(int id);

	@Query("select Count(t) from Tactic t where t.strategy.id = :id")
	Integer countTacticsFromStrategyId(int id);

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select distinct p from ProjectMember pm join pm.project p where p.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = acme.entities.levelb.MemberRole.FUNDRAISER order by p.title asc")
	Collection<Project> findAvailableProjectsByMemberUserAccountId(int userAccountId);

	@Query("select count(pm) from ProjectMember pm where pm.project.id = :projectId and pm.project.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = acme.entities.levelb.MemberRole.FUNDRAISER")
	Long countDraftMembershipByProjectAndUserAccountId(int projectId, int userAccountId);
}
