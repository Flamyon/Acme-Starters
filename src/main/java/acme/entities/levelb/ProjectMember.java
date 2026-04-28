package acme.entities.levelb;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.principals.UserAccount;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "project_member", uniqueConstraints = @UniqueConstraint(columnNames = {
	"project_id", "user_account_id", "role_kind"
}))
@Getter
@Setter
public class ProjectMember extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Project project;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private UserAccount userAccount;

	@Mandatory
	@Column(name = "role_kind")
	@Enumerated(EnumType.STRING)
	private MemberRole roleKind;


	@Transient
	public String getMemberFullName() {
		String result;

		result = this.userAccount != null && this.userAccount.getIdentity() != null ? this.userAccount.getIdentity().getFullName() : "-";

		return result;
	}

	@Transient
	public String getMemberEmail() {
		String result;

		result = this.userAccount != null && this.userAccount.getIdentity() != null ? this.userAccount.getIdentity().getEmail() : "-";

		return result;
	}

	@Transient
	public String getRoleLabel() {
		String result;

		if (this.roleKind == null)
			result = "-";
		else
			switch (this.roleKind) {
			case INVENTOR:
				result = "Inventor";
				break;
			case SPOKESPERSON:
				result = "Spokesperson";
				break;
			case FUNDRAISER:
				result = "Fundraiser";
				break;
			default:
				result = this.roleKind.toString();
				break;
			}

		return result;
	}

}
