package acme.features.student1.inventor.invention;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.levelb.Project;
import acme.entities.student1.Invention;
import acme.entities.student1.Part;

@Repository
public interface InventorInventionRepository extends AbstractRepository {

	@Query("select i from Invention i where i.ticker = :ticker")
	Invention findInventionByTicker(String ticker);

	@Query("select i from Invention i where i.id = :id")
	Invention findInventionById(int id);

	@Query("select i from Invention i where i.inventor.id = :id")
	Collection<Invention> findInventionsByInventorId(int id);

	@Query("select p from Part p where p.invention.id = :id")
	Collection<Part> findPartsByInventionId(int id);

	@Query("select count(p) from Part p where p.invention.id = :id")
	Integer countPartsFromInventionId(int id);

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select distinct p from Project p join p.members pm where p.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = acme.entities.levelb.MemberRole.INVENTOR order by p.title asc")
	Collection<Project> findAvailableProjectsByMemberUserAccountId(int userAccountId);

	@Query("select count(pm) from ProjectMember pm where pm.project.id = :projectId and pm.project.draftMode = true and pm.userAccount.id = :userAccountId and pm.roleKind = acme.entities.levelb.MemberRole.INVENTOR")
	Long countDraftMembershipByProjectAndUserAccountId(int projectId, int userAccountId);
}
