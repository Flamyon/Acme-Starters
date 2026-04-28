package acme.features.levelb.manager.managerdashboard;

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
		List<Integer> projectIds;
		List<Double> projectEfforts;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		totalProjects = this.repository.totalProjectsByManagerId(managerId);
		averageProjectsPerOtherManagers = this.repository.averageProjectsPerOtherManagers(managerId);
		projectIds = this.repository.projectIdsByManagerId(managerId);
		projectEfforts = this.computeProjectEfforts(projectIds);

		this.dashboard = super.newObject(ManagerDashboard.class);
		this.dashboard.setTotalProjects(totalProjects == null ? 0L : totalProjects);
		this.dashboard.setDeviationFromOtherManagersAverage(this.dashboard.getTotalProjects().doubleValue() - (averageProjectsPerOtherManagers == null ? 0.0 : averageProjectsPerOtherManagers));
		this.dashboard.setMinProjectEffort(this.computeMin(projectEfforts));
		this.dashboard.setMaxProjectEffort(this.computeMax(projectEfforts));
		this.dashboard.setAvgProjectEffort(this.computeAverage(projectEfforts));
		this.dashboard.setDevProjectEffort(this.computePopulationStandardDeviation(projectEfforts));
	}

	private List<Double> computeProjectEfforts(final List<Integer> projectIds) {
		List<Double> result;

		result = new java.util.ArrayList<>();

		if (projectIds != null)
			for (Integer projectId : projectIds)
				if (projectId != null)
					result.add(this.computeProjectEffort(projectId.intValue()));

		return result;
	}

	private Double computeProjectEffort(final int projectId) {
		Long memberCount;
		double memberCountValue;
		double totalMonths;
		double result;

		memberCount = this.repository.memberCountByProjectId(projectId);
		memberCountValue = memberCount == null ? 0.0 : memberCount.doubleValue();

		if (memberCountValue == 0.0)
			result = 0.0;
		else {
			totalMonths = 0.0;
			totalMonths += this.sumRoundedMonths(this.repository.inventionDurationsInDaysByProjectId(projectId));
			totalMonths += this.sumRoundedMonths(this.repository.campaignDurationsInDaysByProjectId(projectId));
			totalMonths += this.sumRoundedMonths(this.repository.strategyDurationsInDaysByProjectId(projectId));
			result = totalMonths / memberCountValue;
		}

		return result;
	}

	private double sumRoundedMonths(final List<Number> durationsInDays) {
		double result;

		result = 0.0;

		if (durationsInDays != null)
			for (Number durationInDays : durationsInDays)
				if (durationInDays != null)
					result += this.roundToOneDecimal(durationInDays.doubleValue() / 30.0);

		return result;
	}

	private double roundToOneDecimal(final double value) {
		double result;

		result = Math.round(value * 10.0) / 10.0;

		return result;
	}

	private Double computeMin(final List<Double> values) {
		double result;

		if (values == null || values.isEmpty())
			result = 0.0;
		else {
			result = Double.POSITIVE_INFINITY;
			for (Double value : values)
				if (value != null)
					result = Math.min(result, value.doubleValue());
			if (result == Double.POSITIVE_INFINITY)
				result = 0.0;
		}

		return result;
	}

	private Double computeMax(final List<Double> values) {
		double result;

		if (values == null || values.isEmpty())
			result = 0.0;
		else {
			result = Double.NEGATIVE_INFINITY;
			for (Double value : values)
				if (value != null)
					result = Math.max(result, value.doubleValue());
			if (result == Double.NEGATIVE_INFINITY)
				result = 0.0;
		}

		return result;
	}

	private Double computeAverage(final List<Double> values) {
		double sum;
		int count;
		double result;

		sum = 0.0;
		count = 0;

		if (values != null)
			for (Double value : values)
				if (value != null) {
					sum += value.doubleValue();
					count++;
				}

		if (count == 0)
			result = 0.0;
		else
			result = sum / count;

		return result;
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
