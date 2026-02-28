
package acme.entities.student4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Donation extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	// @ValidHeader: NO está en los componentes del framework
	@Column
	private String				name;

	@Mandatory
	// @ValidText: NO está en los componentes del framework
	@Column
	private String				notes;

	@Mandatory
	@ValidMoney
	@Column
	private Money				money;

	@Mandatory
	@Valid
	@Column
	private DonationKind		kind;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Sponsorship			sponsorship;

}
