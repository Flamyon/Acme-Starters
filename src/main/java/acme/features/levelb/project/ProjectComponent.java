package acme.features.levelb.project;

import java.util.Date;

import acme.client.components.basis.AbstractObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectComponent extends AbstractObject {

	private static final long serialVersionUID = 1L;

	public enum Kind {
		INVENTION,
		CAMPAIGN,
		STRATEGY
	}

	private int id;

	private Kind kind;

	private int sourceId;

	private int projectId;

	private String ticker;

	private String name;

	private String description;

	private Date startMoment;

	private Date endMoment;

	private String moreInfo;

	private Boolean draftMode;

	private String owner;

	public String getKindLabel() {
		String result;

		if (this.kind == null)
			result = "-";
		else
			switch (this.kind) {
			case INVENTION:
				result = "Invention";
				break;
			case CAMPAIGN:
				result = "Campaign";
				break;
			case STRATEGY:
				result = "Strategy";
				break;
			default:
				result = this.kind.name();
				break;
			}

		return result;
	}

}