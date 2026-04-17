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

	@Query("select coalesce(min(case when (select count(distinct pm.member.id) from ProjectMember pm where pm.project.id = p.id) = 0 then 0.0 else (coalesce((select sum(function('round', function('datediff', i.endMoment, i.startMoment) / 30.0, 1)) from Invention i where i.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', c.endMoment, c.startMoment) / 30.0, 1)) from Campaign c where c.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', s.endMoment, s.startMoment) / 30.0, 1)) from Strategy s where s.project.id = p.id), 0.0)) / (select count(distinct pm2.member.id) from ProjectMember pm2 where pm2.project.id = p.id) end), 0.0) from Project p where p.manager.id = :managerId")
	Double minProjectEffortByManagerId(@Param("managerId") int managerId);

	@Query("select coalesce(max(case when (select count(distinct pm.member.id) from ProjectMember pm where pm.project.id = p.id) = 0 then 0.0 else (coalesce((select sum(function('round', function('datediff', i.endMoment, i.startMoment) / 30.0, 1)) from Invention i where i.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', c.endMoment, c.startMoment) / 30.0, 1)) from Campaign c where c.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', s.endMoment, s.startMoment) / 30.0, 1)) from Strategy s where s.project.id = p.id), 0.0)) / (select count(distinct pm2.member.id) from ProjectMember pm2 where pm2.project.id = p.id) end), 0.0) from Project p where p.manager.id = :managerId")
	Double maxProjectEffortByManagerId(@Param("managerId") int managerId);

	@Query("select coalesce(avg(case when (select count(distinct pm.member.id) from ProjectMember pm where pm.project.id = p.id) = 0 then 0.0 else (coalesce((select sum(function('round', function('datediff', i.endMoment, i.startMoment) / 30.0, 1)) from Invention i where i.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', c.endMoment, c.startMoment) / 30.0, 1)) from Campaign c where c.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', s.endMoment, s.startMoment) / 30.0, 1)) from Strategy s where s.project.id = p.id), 0.0)) / (select count(distinct pm2.member.id) from ProjectMember pm2 where pm2.project.id = p.id) end), 0.0) from Project p where p.manager.id = :managerId")
	Double avgProjectEffortByManagerId(@Param("managerId") int managerId);

	@Query("select case when (select count(distinct pm.member.id) from ProjectMember pm where pm.project.id = p.id) = 0 then 0.0 else (coalesce((select sum(function('round', function('datediff', i.endMoment, i.startMoment) / 30.0, 1)) from Invention i where i.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', c.endMoment, c.startMoment) / 30.0, 1)) from Campaign c where c.project.id = p.id), 0.0) + coalesce((select sum(function('round', function('datediff', s.endMoment, s.startMoment) / 30.0, 1)) from Strategy s where s.project.id = p.id), 0.0)) / (select count(distinct pm2.member.id) from ProjectMember pm2 where pm2.project.id = p.id) end from Project p where p.manager.id = :managerId")
	List<Double> projectEffortsByManagerId(@Param("managerId") int managerId);

}
