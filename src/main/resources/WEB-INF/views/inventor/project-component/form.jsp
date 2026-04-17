<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="inventor.project-components.form.label.kind" path="kindLabel" readonly="true"/>
	<acme:form-textbox code="inventor.project-components.form.label.ticker" path="ticker" readonly="true"/>
	<acme:form-textbox code="inventor.project-components.form.label.name" path="name" readonly="true"/>
	<acme:form-textbox code="inventor.project-components.form.label.owner" path="owner" readonly="true"/>
	<acme:form-checkbox code="inventor.project-components.form.label.draft" path="draftMode" readonly="true"/>
	<acme:form-moment code="inventor.project-components.form.label.start" path="startMoment" readonly="true"/>
	<acme:form-moment code="inventor.project-components.form.label.end" path="endMoment" readonly="true"/>
	<acme:form-textarea code="inventor.project-components.form.label.description" path="description" readonly="true"/>
	<acme:form-url code="inventor.project-components.form.label.moreInfo" path="moreInfo" readonly="true"/>

	<acme:button code="inventor.project-components.form.button.return-list" action="/inventor/project-component/list?projectId=${projectId}"/>
	<acme:button code="inventor.project-components.form.button.return-project" action="/inventor/project/show?id=${projectId}"/>
</acme:form>