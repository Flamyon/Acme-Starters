<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${_command == 'create'}">
		<acme:form-select code="manager.project-member.form.label.nominee" path="nominee" choices="${nominees}"/>
		<acme:submit code="manager.project-member.form.button.create" action="/manager/project-member/create?projectId=${projectId}"/>
	</jstl:if>

	<jstl:if test="${_command != 'create'}">
		<acme:form-textbox code="manager.project-member.form.label.fullName" path="memberFullName" readonly="true"/>
		<acme:form-textbox code="manager.project-member.form.label.email" path="memberEmail" readonly="true"/>
		<acme:form-textbox code="manager.project-member.form.label.role" path="roleLabel" readonly="true"/>
		<jstl:if test="${draftMode}">
			<acme:submit code="manager.project-member.form.button.delete" action="/manager/project-member/delete"/>
		</jstl:if>
	</jstl:if>

	<acme:button code="manager.project-member.form.button.return" action="/manager/project-member/list?projectId=${projectId}"/>
</acme:form>
