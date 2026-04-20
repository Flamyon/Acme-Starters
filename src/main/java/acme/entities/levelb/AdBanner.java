package acme.entities.levelb;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AdBanner extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidText
	@Column
	private String				slogan;

	@Mandatory
	@ValidUrl
	@Column
	private String				targetUrl;

	@Mandatory
	@ValidUrl
	@Column
	private String				picture;

}
