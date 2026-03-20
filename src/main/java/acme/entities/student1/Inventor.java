
package acme.entities.student1;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Inventor extends AbstractRole {

	// Serialisation version 

	private static final long	serialVersionUID	= 1L;

	// Atributos

	@Mandatory
	@ValidText
	@Column
	private String				bio;

	@Mandatory
	@ValidText
	@Column
	private String				keyWords;

	@Mandatory
	@Column
	private Boolean				licensed;

}
