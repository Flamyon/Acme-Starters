package acme.features.levelb.manager.projectmember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.ProjectMember;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student3.Strategy;
import acme.realms.Manager;

@Service
public class ManagerProjectMemberDeleteService extends AbstractService<Manager, ProjectMember> {

	@Autowired
	private ProjectRepository repository;

	private ProjectMember projectMember;


	@Override
	public void load() {
		Integer id;

		this.projectMember = super.newObject(ProjectMember.class);
		id = super.getRequest().getData("id", Integer.class, null);
		if (id == null || id.intValue() == 0)
			return;
		this.projectMember = this.repository.findProjectMemberById(id.intValue());
		if (this.projectMember == null)
			this.projectMember = super.newObject(ProjectMember.class);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.projectMember != null && this.projectMember.getId() != 0 && this.projectMember.getProject() != null && this.projectMember.getProject().getManager() != null && this.projectMember.getProject().getManager().isPrincipal() && Boolean.TRUE.equals(this.projectMember.getProject().getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
	}

	@Override
	public void validate() {
	}

	@Override
	@Transactional
	public void execute() {
		int projectId;
		int userAccountId;
		MemberRole roleKind;

		if (this.projectMember.getId() == 0 || this.projectMember.getProject() == null || this.projectMember.getUserAccount() == null)
			return;

		projectId = this.projectMember.getProject().getId();
		userAccountId = this.projectMember.getUserAccount().getId();
		roleKind = this.projectMember.getRoleKind();

		if (roleKind != null)
			switch (roleKind) {
			case INVENTOR:
				this.detachInventorComponents(projectId, userAccountId);
				break;
			case SPOKESPERSON:
				this.detachSpokespersonComponents(projectId, userAccountId);
				break;
			case FUNDRAISER:
				this.detachFundraiserComponents(projectId, userAccountId);
				break;
			default:
				break;
			}

		this.repository.delete(this.projectMember);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.projectMember, "memberFullName", "memberEmail", "roleLabel");
		tuple.put("projectId", this.projectMember.getProject() == null ? 0 : this.projectMember.getProject().getId());
		tuple.put("draftMode", this.projectMember.getProject() == null ? false : this.projectMember.getProject().getDraftMode());
	}

	private void detachInventorComponents(final int projectId, final int userAccountId) {
		Collection<Invention> inventions;

		inventions = this.repository.findInventionsByProjectIdAndInventorUserAccountId(projectId, userAccountId);
		for (Invention invention : inventions)
			invention.setProject(null);

		this.repository.saveAll(inventions);
	}

	private void detachSpokespersonComponents(final int projectId, final int userAccountId) {
		Collection<Campaign> campaigns;

		campaigns = this.repository.findCampaignsByProjectIdAndSpokespersonUserAccountId(projectId, userAccountId);
		for (Campaign campaign : campaigns)
			campaign.setProject(null);

		this.repository.saveAll(campaigns);
	}

	private void detachFundraiserComponents(final int projectId, final int userAccountId) {
		Collection<Strategy> strategies;

		strategies = this.repository.findStrategiesByProjectIdAndFundraiserUserAccountId(projectId, userAccountId);
		for (Strategy strategy : strategies)
			strategy.setProject(null);

		this.repository.saveAll(strategies);
	}

}
