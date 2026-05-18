
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobOffer extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private long				id;
	private String				title;
	private String				companyName;
	private String				description;
	private String				url;
	private int					score;
}
