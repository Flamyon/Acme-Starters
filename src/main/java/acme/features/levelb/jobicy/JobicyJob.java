package acme.features.levelb.jobicy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobicyJob {
	private String jobTitle;
	private String companyName;
	private String jobExcerpt;
	private String jobDescription;
	private String url;
	private String id;
}
