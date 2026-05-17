package acme.features.levelb.spokesperson.project;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student2.Spokesperson;
import acme.features.levelb.jobicy.JobicyExchange;
import acme.forms.JobOffer;

@Service
public class SpokespersonProjectJobOfferListService extends AbstractService<Spokesperson, JobOffer> {

	@Autowired
	private ProjectRepository	repository;

	@Autowired
	private JobicyExchange		jobicyExchange;

	private Project				project;
	private Collection<JobOffer>	jobOffers;

	@Override
	public void load() {
		final int projectId = super.getRequest().getData("projectId", int.class);
		this.jobOffers = List.of();
		this.project = this.repository.findProjectByIdWithDetails(projectId);
		if (this.project != null) {
			this.jobOffers = this.jobicyExchange.fetchTop10JobOffers(this.project.getKeywords());
		}
	}

	@Override
	public void authorise() {
		boolean status;
		final int userAccountId = super.getRequest().getPrincipal().getAccountId();
		final boolean isMember = this.project != null && this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), userAccountId, MemberRole.SPOKESPERSON) > 0;
		status = this.project != null && (Boolean.FALSE.equals(this.project.getDraftMode()) || isMember);

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.jobOffers, "title", "companyName", "description", "url");
		super.unbindGlobal("projectId", this.project == null ? 0 : this.project.getId());
	}
}
