package acme.forms;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobOffer extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private String title;
	private String companyName;
	private String description;
	private String url;

	private int score; // Transient for sorting
}
