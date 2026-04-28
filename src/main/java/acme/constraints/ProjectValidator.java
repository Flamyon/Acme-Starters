package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;

@Validator
public class ProjectValidator extends AbstractValidator<ValidProject, Project> {

	@Autowired
	private ProjectRepository	repository;


	@Override
	protected void initialise(final ValidProject annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Project project, final ConstraintValidatorContext context) {
		assert context != null;

		if (project == null)
			return true;

		boolean isValid = true;

		isValid &= this.validateDates(project, context);
		isValid &= this.validateKeywords(project, context);

		if (Boolean.FALSE.equals(project.getDraftMode()))
			isValid &= this.validateHasInventions(project, context);

		return isValid;
	}

	private boolean validateDates(final Project project, final ConstraintValidatorContext context) {
		boolean isValid = true;

		if (project.getKickOff() != null && project.getCloseOut() != null) {
			boolean validInterval = MomentHelper.isBefore(project.getKickOff(), project.getCloseOut());
			super.state(context, validInterval, "closeOut", "acme.validation.project.invalid-interval.message");
			isValid &= validInterval;
		}

		return isValid;
	}

	private boolean validateKeywords(final Project project, final ConstraintValidatorContext context) {
		boolean isValid = true;
		String keywords = project.getKeywords();

		if (keywords == null || keywords.trim().isEmpty())
			return true;

		String[] tokens = keywords.split(",", -1);
		boolean validFormat = true;

		for (String token : tokens)
			validFormat &= token != null && !token.trim().isEmpty();

		super.state(context, validFormat, "keywords", "acme.validation.project.invalid-keywords.message");
		isValid &= validFormat;

		if (validFormat) {
			boolean shortList = tokens.length <= 10;
			super.state(context, shortList, "keywords", "acme.validation.project.too-many-keywords.message");
			isValid &= shortList;
		}

		return isValid;
	}

	private boolean validateHasInventions(final Project project, final ConstraintValidatorContext context) {
		boolean hasInventions;

		if (project.getId() == 0)
			hasInventions = false;
		else {
			Long inventionCount;

			inventionCount = this.repository.countInventionsByProjectId(project.getId());
			hasInventions = inventionCount != null && inventionCount > 0;
		}

		super.state(context, hasInventions, "draftMode", "acme.validation.project.no-inventions.message");

		return hasInventions;
	}

}
