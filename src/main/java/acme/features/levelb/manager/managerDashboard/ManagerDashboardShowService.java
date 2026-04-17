package acme.features.levelb.manager.managerDashboard;

import java.util.List;

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
		List<Double> projectEfforts;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		totalProjects = this.repository.totalProjectsByManagerId(managerId);
		averageProjectsPerOtherManagers = this.repository.averageProjectsPerOtherManagers(managerId);
		projectEfforts = this.repository.projectEffortsByManagerId(managerId);

		this.dashboard = super.newObject(ManagerDashboard.class);
		this.dashboard.setTotalProjects(totalProjects == null ? 0L : totalProjects);
		this.dashboard.setDeviationFromOtherManagersAverage(this.dashboard.getTotalProjects().doubleValue() - (averageProjectsPerOtherManagers == null ? 0.0 : averageProjectsPerOtherManagers));
		this.dashboard.setMinProjectEffort(this.repository.minProjectEffortByManagerId(managerId));
		this.dashboard.setMaxProjectEffort(this.repository.maxProjectEffortByManagerId(managerId));
		this.dashboard.setAvgProjectEffort(this.repository.avgProjectEffortByManagerId(managerId));
		this.dashboard.setDevProjectEffort(this.computePopulationStandardDeviation(projectEfforts));
	}

	private Double computePopulationStandardDeviation(final List<Double> values) {
		double sum;
		double sumSquares;
		int count;
		double mean;
		double variance;
		double result;

		sum = 0.0;
		sumSquares = 0.0;
		count = 0;

		if (values != null)
			for (Double value : values)
				if (value != null) {
					sum += value.doubleValue();
					sumSquares += value.doubleValue() * value.doubleValue();
					count++;
				}

		if (count == 0)
			result = 0.0;
		else {
			mean = sum / count;
			variance = sumSquares / count - mean * mean;
			result = Math.sqrt(Math.max(variance, 0.0));
		}

		return result;
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
