package acme.features.student5.auditor.auditReport;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.student5.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportShowAssociateProjectService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository	repository;

	private AuditReport					entity;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.entity = this.repository.findAuditReportById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entity != null && this.entity.getAuditor() != null && this.entity.getAuditor().isPrincipal() && Boolean.FALSE.equals(this.entity.getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("hours", this.entity.getHours());
		this.putProjectData(tuple);
	}

	private void putProjectData(final Tuple tuple) {
		assert tuple != null;

		Collection<Project> projects;
		Project selectedProject;
		Project visibleProject;
		SelectChoices projectChoices;
		String selectedKey;
		boolean selectedIncluded;
		boolean isOwner;

		projects = this.repository.findPublishedProjects();
		if (projects == null)
			projects = new ArrayList<>();

		selectedProject = this.entity.getProject();
		selectedIncluded = false;

		if (selectedProject != null)
			for (Project candidate : projects)
				if (candidate != null && candidate.getId() == selectedProject.getId()) {
					selectedIncluded = true;
					break;
				}

		if (!selectedIncluded)
			selectedProject = null;

		visibleProject = selectedProject;
		selectedKey = visibleProject == null ? "0" : Integer.toString(visibleProject.getId());
		projectChoices = new SelectChoices();
		projectChoices.add("0", "---", "0".equals(selectedKey));
		for (Project project : projects) {
			String key;
			String label;

			key = Integer.toString(project.getId());
			label = project.getTitle();
			projectChoices.add(key, label, key.equals(selectedKey));
		}
		isOwner = this.entity.getAuditor() != null && this.entity.getAuditor().isPrincipal();

		tuple.put("isOwner", isOwner);
		tuple.put("project", visibleProject == null ? 0 : visibleProject.getId());
		tuple.put("projectId", visibleProject == null ? 0 : visibleProject.getId());
		tuple.put("projectTitle", visibleProject == null ? "-" : visibleProject.getTitle());
		tuple.put("projects", projectChoices);
	}
}
