package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Long				totalProjects;
	Double				deviationFromOtherManagersAverage;
	Double				minProjectEffort;
	Double				maxProjectEffort;
	Double				avgProjectEffort;
	Double				devProjectEffort;

}