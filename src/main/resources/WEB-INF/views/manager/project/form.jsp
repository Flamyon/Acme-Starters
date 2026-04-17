<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="manager.project.form.label.title" path="title" readonly="${!draftMode}"/>
	<acme:form-textbox code="manager.project.form.label.keywords" path="keywords" readonly="${!draftMode}"/>
	<acme:form-textarea code="manager.project.form.label.description" path="description" readonly="${!draftMode}"/>
	<acme:form-moment code="manager.project.form.label.kickOff" path="kickOff" readonly="${!draftMode}"/>
	<acme:form-moment code="manager.project.form.label.closeOut" path="closeOut" readonly="${!draftMode}"/>

	<acme:form-textarea code="manager.project.form.label.members" path="membersSummary" readonly="true"/>
	<acme:form-textarea code="manager.project.form.label.inventions" path="inventionsSummary" readonly="true"/>
	<acme:form-textarea code="manager.project.form.label.campaigns" path="campaignsSummary" readonly="true"/>
	<acme:form-textarea code="manager.project.form.label.strategies" path="strategiesSummary" readonly="true"/>

	<jstl:if test="${_command != 'create'}">
		<acme:button code="manager.project.form.button.members" action="/manager/project-member/list?projectId=${id}"/>
	</jstl:if>

	<jstl:if test="${_command == 'create'}">
		<acme:submit code="manager.project.form.button.create" action="/manager/project/create"/>
	</jstl:if>

	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode}">
		<acme:submit code="manager.project.form.button.update" action="/manager/project/update"/>
		<acme:submit code="manager.project.form.button.delete" action="/manager/project/delete"/>
		<acme:submit code="manager.project.form.button.publish" action="/manager/project/publish"/>
	</jstl:if>
</acme:form>
