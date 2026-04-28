package acme.features.student4.sponsor.sponsorship;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.student4.Sponsorship;
import acme.entities.student4.SponsorshipRepository;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipShowAssociateProjectService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorshipRepository	repository;

	private Sponsorship				sponsorship;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findSponsorshipById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.sponsorship != null && this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().isPrincipal() && Boolean.FALSE.equals(this.sponsorship.getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive", "totalMoney");
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

		selectedProject = this.sponsorship.getProject();
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
		isOwner = this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().isPrincipal();

		tuple.put("isOwner", isOwner);
		tuple.put("project", visibleProject == null ? 0 : visibleProject.getId());
		tuple.put("projectId", visibleProject == null ? 0 : visibleProject.getId());
		tuple.put("projectTitle", visibleProject == null ? "-" : visibleProject.getTitle());
		tuple.put("projects", projectChoices);
	}
}
