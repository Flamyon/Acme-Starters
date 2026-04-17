<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="fundraiser.project.form.label.title" path="title" readonly="true"/>
	<acme:form-textbox code="fundraiser.project.form.label.manager" path="manager.identity.fullName" readonly="true"/>
	<acme:form-textbox code="fundraiser.project.form.label.keywords" path="keywords" readonly="true"/>
	<acme:form-textarea code="fundraiser.project.form.label.description" path="description" readonly="true"/>
	<acme:form-moment code="fundraiser.project.form.label.kickOff" path="kickOff" readonly="true"/>
	<acme:form-moment code="fundraiser.project.form.label.closeOut" path="closeOut" readonly="true"/>
	<acme:form-textarea code="fundraiser.project.form.label.members" path="membersSummary" readonly="true"/>
	<acme:form-textarea code="fundraiser.project.form.label.inventions" path="inventionsSummary" readonly="true"/>
	<acme:form-textarea code="fundraiser.project.form.label.campaigns" path="campaignsSummary" readonly="true"/>
	<acme:form-textarea code="fundraiser.project.form.label.strategies" path="strategiesSummary" readonly="true"/>

	<jstl:if test="${showComponents}">
		<acme:button code="fundraiser.project.form.button.components" action="/fundraiser/project-component/list?projectId=${id}"/>
	</jstl:if>
</acme:form>
