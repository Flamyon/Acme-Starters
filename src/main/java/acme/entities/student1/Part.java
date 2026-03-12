
package acme.entities.student1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Part extends AbstractEntity {

	// Serialisation version 

	private static final long	serialVersionUID	= 1L;

	// Atributos

	@Mandatory
	@ValidText
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoney
	@ValidEur
	@Column
	private Money				cost;

	@Mandatory
	@Valid
	@Column
	private PartKind			kind;

	// Relaciones

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Invention			invention;

}
