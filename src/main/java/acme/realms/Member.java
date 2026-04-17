package acme.realms;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractRole;
import acme.client.components.basis.AbstractSquad;
import acme.client.components.validation.Mandatory;
import acme.entities.student1.Inventor;
import acme.entities.student2.Spokesperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member extends AbstractSquad {

	private static final long serialVersionUID = 1L;


	@Mandatory
	@Transient
	@Override
	public Set<Class<? extends AbstractRole>> getMembers() {
		Set<Class<? extends AbstractRole>> result;

		result = Set.of(Inventor.class, Spokesperson.class, Fundraiser.class);

		return result;
	}

}