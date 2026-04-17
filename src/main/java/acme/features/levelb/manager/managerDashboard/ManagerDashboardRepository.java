package acme.features.levelb.manager.managerDashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select count(p) from Project p where p.manager.id = :managerId")
	Long totalProjectsByManagerId(@Param("managerId") int managerId);

	@Query("select coalesce(avg((select count(p) from Project p where p.manager.id = m.id)), 0.0) from Manager m where m.id <> :managerId")
	Double averageProjectsPerOtherManagers(@Param("managerId") int managerId);

	@Query("select p.id from Project p where p.manager.id = :managerId")
	List<Integer> projectIdsByManagerId(@Param("managerId") int managerId);

	@Query("select function('datediff', i.endMoment, i.startMoment) from Invention i where i.project.id = :projectId")
	List<Number> inventionDurationsInDaysByProjectId(@Param("projectId") int projectId);

	@Query("select function('datediff', c.endMoment, c.startMoment) from Campaign c where c.project.id = :projectId")
	List<Number> campaignDurationsInDaysByProjectId(@Param("projectId") int projectId);

	@Query("select function('datediff', s.endMoment, s.startMoment) from Strategy s where s.project.id = :projectId")
	List<Number> strategyDurationsInDaysByProjectId(@Param("projectId") int projectId);

	@Query("select count(distinct pm.member.id) from ProjectMember pm where pm.project.id = :projectId")
	Long memberCountByProjectId(@Param("projectId") int projectId);

}
