
package acme.features.levelb.manager.projectmember;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acme.client.components.models.Tuple;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectMember;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Inventor;
import acme.entities.student2.Spokesperson;
import acme.realms.Fundraiser;
import acme.realms.Manager;

@Service
public class ManagerProjectMemberCreateService extends AbstractService<Manager, ProjectMember> {

	@Autowired
	private ProjectRepository	repository;

	private ProjectMember		projectMember;

	private Project				project;

	private String				nomineeKey;

	private MemberRole			nomineeRoleKind;

	private UserAccount			nomineeAccount;


	@Override
	public void load() {
		Integer projectId;
		String nomineeData;

		projectId = super.getRequest().getData("projectId", Integer.class, null);
		if (projectId == null || projectId.intValue() == 0)
			this.project = super.newObject(Project.class);
		else {
			this.project = this.repository.findProjectById(projectId.intValue());
			if (this.project == null)
				this.project = super.newObject(Project.class);
		}

		this.projectMember = super.newObject(ProjectMember.class);
		this.projectMember.setProject(this.project);
		this.nomineeKey = null;
		this.nomineeRoleKind = null;
		this.nomineeAccount = null;

		if (super.getRequest().hasData("nominee", String.class)) {
			nomineeData = super.getRequest().getData("nominee", String.class);
			this.nomineeKey = nomineeData != null && !nomineeData.isBlank() && !"0".equals(nomineeData) ? nomineeData : null;

			if (this.nomineeKey != null)
				this.loadNomineeFromKey(this.nomineeKey);
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
	}

	@Override
	public void validate() {
		boolean validNominee;
		boolean uniqueInProject;

		if (this.project.getId() == 0)
			return;

		validNominee = this.nomineeAccount != null && this.nomineeRoleKind != null;
		super.state(validNominee, "nominee", "manager.project-member.form.error.nominee");

		if (!validNominee)
			return;

		uniqueInProject = this.repository.countProjectMemberByProjectIdAndUserAccountIdAndRoleKind(this.project.getId(), this.nomineeAccount.getId(), this.nomineeRoleKind) == 0;

		super.state(uniqueInProject, "nominee", "manager.project-member.form.error.duplicate");
	}

	@Override
	@Transactional
	public void execute() {
		if (this.project.getId() == 0 || this.nomineeAccount == null || this.nomineeRoleKind == null)
			return;

		this.projectMember.setProject(this.project);
		this.projectMember.setUserAccount(this.nomineeAccount);
		this.projectMember.setRoleKind(this.nomineeRoleKind);
		this.repository.save(this.projectMember);
	}

	@Override
	public void unbind() {
		Collection<Inventor> inventors;
		Collection<Spokesperson> spokespersons;
		Collection<Fundraiser> fundraisers;
		List<Inventor> sortedInventors;
		List<Spokesperson> sortedSpokespersons;
		List<Fundraiser> sortedFundraisers;
		List<String> validKeys;
		String selectedNominee;
		SelectChoices choices;
		Tuple tuple;

		if (this.project.getId() == 0) {
			inventors = List.of();
			spokespersons = List.of();
			fundraisers = List.of();
		} else {
			inventors = this.repository.findNomineeInventorsByProjectId(this.project.getId(), MemberRole.INVENTOR);
			spokespersons = this.repository.findNomineeSpokespersonsByProjectId(this.project.getId(), MemberRole.SPOKESPERSON);
			fundraisers = this.repository.findNomineeFundraisersByProjectId(this.project.getId(), MemberRole.FUNDRAISER);
		}

		sortedInventors = inventors.stream().sorted(this.roleComparator(ua -> ua.getUserAccount())).toList();
		sortedSpokespersons = spokespersons.stream().sorted(this.roleComparator(ua -> ua.getUserAccount())).toList();
		sortedFundraisers = fundraisers.stream().sorted(this.roleComparator(ua -> ua.getUserAccount())).toList();

		validKeys = new ArrayList<>();
		sortedInventors.forEach(i -> validKeys.add(this.keyOf(MemberRole.INVENTOR, i.getUserAccount().getId())));
		sortedSpokespersons.forEach(s -> validKeys.add(this.keyOf(MemberRole.SPOKESPERSON, s.getUserAccount().getId())));
		sortedFundraisers.forEach(f -> validKeys.add(this.keyOf(MemberRole.FUNDRAISER, f.getUserAccount().getId())));

		selectedNominee = this.nomineeKey != null && validKeys.contains(this.nomineeKey) ? this.nomineeKey : null;

		choices = new SelectChoices();
		choices.add("0", "----", selectedNominee == null);
		for (Inventor inventor : sortedInventors) {
			String key;
			String label;

			key = this.keyOf(MemberRole.INVENTOR, inventor.getUserAccount().getId());
			label = String.format("%s (Inventor)", this.fullName(inventor.getUserAccount()));
			choices.add(key, label, key.equals(selectedNominee));
		}
		for (Spokesperson spokesperson : sortedSpokespersons) {
			String key;
			String label;

			key = this.keyOf(MemberRole.SPOKESPERSON, spokesperson.getUserAccount().getId());
			label = String.format("%s (Spokesperson)", this.fullName(spokesperson.getUserAccount()));
			choices.add(key, label, key.equals(selectedNominee));
		}
		for (Fundraiser fundraiser : sortedFundraisers) {
			String key;
			String label;

			key = this.keyOf(MemberRole.FUNDRAISER, fundraiser.getUserAccount().getId());
			label = String.format("%s (Fundraiser)", this.fullName(fundraiser.getUserAccount()));
			choices.add(key, label, key.equals(selectedNominee));
		}

		tuple = new Tuple();
		tuple.put("projectId", this.project.getId());
		tuple.put("draftMode", this.project.getDraftMode());
		tuple.put("nominee", choices.getSelected().getKey());
		tuple.put("nominees", choices);
		super.getResponse().addData(tuple);
	}

	private void loadNomineeFromKey(final String nomineeKey) {
		assert nomineeKey != null;

		String[] fragments;
		int userAccountId;

		fragments = nomineeKey.split(":", 2);
		if (fragments.length != 2)
			return;

		try {
			userAccountId = Integer.parseInt(fragments[1]);
		} catch (NumberFormatException ex) {
			return;
		}

		switch (fragments[0]) {
		case "I":
			Inventor inventor;

			inventor = this.repository.findInventorByUserAccountId(userAccountId);
			if (inventor != null) {
				this.nomineeAccount = inventor.getUserAccount();
				this.nomineeRoleKind = MemberRole.INVENTOR;
			}
			break;
		case "S":
			Spokesperson spokesperson;

			spokesperson = this.repository.findSpokespersonByUserAccountId(userAccountId);
			if (spokesperson != null) {
				this.nomineeAccount = spokesperson.getUserAccount();
				this.nomineeRoleKind = MemberRole.SPOKESPERSON;
			}
			break;
		case "F":
			Fundraiser fundraiser;

			fundraiser = this.repository.findFundraiserByUserAccountId(userAccountId);
			if (fundraiser != null) {
				this.nomineeAccount = fundraiser.getUserAccount();
				this.nomineeRoleKind = MemberRole.FUNDRAISER;
			}
			break;
		default:
			break;
		}
	}

	private <T> Comparator<T> roleComparator(final java.util.function.Function<T, UserAccount> accountAccessor) {
		return Comparator.comparing((final T element) -> this.surnameOf(accountAccessor.apply(element))).thenComparing(element -> this.nameOf(accountAccessor.apply(element)));
	}

	private String keyOf(final MemberRole roleKind, final int userAccountId) {
		assert roleKind != null;

		String prefix;

		switch (roleKind) {
		case INVENTOR:
			prefix = "I";
			break;
		case SPOKESPERSON:
			prefix = "S";
			break;
		case FUNDRAISER:
			prefix = "F";
			break;
		default:
			prefix = "?";
			break;
		}

		return String.format("%s:%d", prefix, userAccountId);
	}

	private String fullName(final UserAccount userAccount) {
		String result;

		result = userAccount != null && userAccount.getIdentity() != null && userAccount.getIdentity().getFullName() != null ? userAccount.getIdentity().getFullName() : "-";

		return result;
	}

	private String surnameOf(final UserAccount userAccount) {
		String result;

		result = userAccount != null && userAccount.getIdentity() != null && userAccount.getIdentity().getSurname() != null ? userAccount.getIdentity().getSurname() : "";

		return result;
	}

	private String nameOf(final UserAccount userAccount) {
		String result;

		result = userAccount != null && userAccount.getIdentity() != null && userAccount.getIdentity().getName() != null ? userAccount.getIdentity().getName() : "";

		return result;
	}

}
