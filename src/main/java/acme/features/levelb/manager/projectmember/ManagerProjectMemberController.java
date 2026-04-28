package acme.features.levelb.manager.projectmember;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.levelb.ProjectMember;
import acme.realms.Manager;

@Controller
public class ManagerProjectMemberController extends AbstractController<Manager, ProjectMember> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", ManagerProjectMemberListService.class);
		super.addBasicCommand("show", ManagerProjectMemberShowService.class);
		super.addBasicCommand("create", ManagerProjectMemberCreateService.class);
		super.addBasicCommand("delete", ManagerProjectMemberDeleteService.class);
	}

}
