<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.project.form.label.title" path="title" readonly="true"/>
	<acme:form-textbox code="any.project.form.label.manager" path="manager.identity.fullName" readonly="true"/>
	<acme:form-textbox code="any.project.form.label.keywords" path="keywords" readonly="true"/>
	<acme:form-textarea code="any.project.form.label.description" path="description" readonly="true"/>
	<acme:form-moment code="any.project.form.label.kickOff" path="kickOff" readonly="true"/>
	<acme:form-moment code="any.project.form.label.closeOut" path="closeOut" readonly="true"/>
	<acme:form-textarea code="any.project.form.label.members" path="membersSummary" readonly="true"/>
	<acme:form-textarea code="any.project.form.label.inventions" path="inventionsSummary" readonly="true"/>
	<acme:form-textarea code="any.project.form.label.campaigns" path="campaignsSummary" readonly="true"/>
	<acme:form-textarea code="any.project.form.label.strategies" path="strategiesSummary" readonly="true"/>
	<acme:form-textarea code="any.project.form.label.sponsorships" path="sponsorshipsSummary" readonly="true"/>
	<acme:form-textarea code="any.project.form.label.auditReports" path="auditReportsSummary" readonly="true"/>
</acme:form>
