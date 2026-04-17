
package acme.entities.levelb;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.constraints.ValidProject;
import acme.constraints.ValidText;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student3.Strategy;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@ValidProject
@Getter
@Setter
public class Project extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidText
	@Column
	private String				title;

	@Mandatory
	@ValidText
	@Column
	private String				keywords;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				kickOff;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				closeOut;

	@Mandatory
	@Valid
	@Column
	private Boolean				draftMode;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;

	@Valid
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProjectMember>	members			= new HashSet<>();

	@Valid
	@OneToMany(mappedBy = "project")
	private Set<Invention>		inventions		= new HashSet<>();

	@Valid
	@OneToMany(mappedBy = "project")
	private Set<Campaign>		campaigns		= new HashSet<>();

	@Valid
	@OneToMany(mappedBy = "project")
	private Set<Strategy>		strategies		= new HashSet<>();

}
