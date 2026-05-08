package acme.features.levelb.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.entities.levelb.ProjectMember;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student3.Strategy;
import acme.entities.student4.Sponsorship;
import acme.entities.student5.AuditReport;
import acme.features.levelb.project.ProjectSupport;
import acme.realms.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	@Autowired
	private ProjectRepository repository;

	private Project project;


	@Override
	public void load() {
		Integer id;

		id = super.getRequest().getData("id", Integer.class, null);
		if (id == null || id.intValue() == 0)
			this.project = super.newObject(Project.class);
		else {
			this.project = this.repository.findProjectByIdWithDetails(id.intValue());
			if (this.project == null)
				this.project = super.newObject(Project.class);
		}
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.project != null && this.project.getId() != 0 && this.project.getManager() != null && this.project.getManager().isPrincipal() && Boolean.TRUE.equals(this.project.getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut");
	}

	@Override
	public void validate() {
		// No field validation on delete.
	}

	@Override
	public void execute() {
		Collection<ProjectMember> members;
		Collection<Invention> inventions;
		Collection<Campaign> campaigns;
		Collection<Strategy> strategies;
		Collection<Sponsorship> sponsorships;
		Collection<AuditReport> auditReports;

		if (this.project.getId() == 0)
			return;

		members = this.repository.findProjectMembersByProjectId(this.project.getId());
		inventions = this.repository.findInventionsByProjectId(this.project.getId());
		campaigns = this.repository.findCampaignsByProjectId(this.project.getId());
		strategies = this.repository.findStrategiesByProjectId(this.project.getId());
		sponsorships = this.repository.findSponsorshipsByProjectId(this.project.getId());
		auditReports = this.repository.findAuditReportsByProjectId(this.project.getId());

		inventions.forEach(i -> i.setProject(null));
		campaigns.forEach(c -> c.setProject(null));
		strategies.forEach(s -> s.setProject(null));
		sponsorships.forEach(sp -> sp.setProject(null));
		auditReports.forEach(ar -> ar.setProject(null));

		this.repository.deleteAll(members);
		this.repository.saveAll(inventions);
		this.repository.saveAll(campaigns);
		this.repository.saveAll(strategies);
		this.repository.saveAll(sponsorships);
		this.repository.saveAll(auditReports);
		this.repository.delete(this.project);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode");
		tuple.put("id", this.project.getId() != 0 ? this.project.getId() : 0);
		ProjectSupport.putDetails(tuple, this.project, this.repository);
	}

}
