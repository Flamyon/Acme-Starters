package acme.features.levelb.manager.managerDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@Service
public class ManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	@Autowired
	private ManagerDashboardRepository repository;

	private ManagerDashboard		dashboard;


	@Override
	public void load() {
		int managerId;
		Long totalProjects;
		Double averageProjectsPerOtherManagers;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		totalProjects = this.repository.totalProjectsByManagerId(managerId);
		averageProjectsPerOtherManagers = this.repository.averageProjectsPerOtherManagers(managerId);

		this.dashboard = super.newObject(ManagerDashboard.class);
		this.dashboard.setTotalProjects(totalProjects == null ? 0L : totalProjects);
		this.dashboard.setDeviationFromOtherManagersAverage(this.dashboard.getTotalProjects().doubleValue() - (averageProjectsPerOtherManagers == null ? 0.0 : averageProjectsPerOtherManagers));
		this.dashboard.setMinProjectEffort(this.repository.minProjectEffortByManagerId(managerId));
		this.dashboard.setMaxProjectEffort(this.repository.maxProjectEffortByManagerId(managerId));
		this.dashboard.setAvgProjectEffort(this.repository.avgProjectEffortByManagerId(managerId));
		this.dashboard.setDevProjectEffort(this.repository.devProjectEffortByManagerId(managerId));
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.dashboard, "totalProjects", "deviationFromOtherManagersAverage", "minProjectEffort", "maxProjectEffort", "avgProjectEffort", "devProjectEffort");
	}

}
