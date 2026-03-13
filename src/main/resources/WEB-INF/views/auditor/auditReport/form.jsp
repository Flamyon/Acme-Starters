<%--
- form.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="auditor.auditReport.form.label.ticker" path="ticker" readonly="${!draftMode}"/>
	<acme:form-textbox code="auditor.auditReport.form.label.name" path="name" readonly="${!draftMode}"/>
	<acme:form-textbox code="auditor.auditReport.form.label.description" path="description" readonly="${!draftMode}"/>
	<acme:form-moment code="auditor.auditReport.form.label.startMoment" path="startMoment" readonly="${!draftMode}"/>
	<acme:form-moment code="auditor.auditReport.form.label.endMoment" path="endMoment" readonly="${!draftMode}"/>
	<acme:form-url code="auditor.auditReport.form.label.moreInfo" path="moreInfo" readonly="${!draftMode}"/>
	
	<jstl:if test="${_command != 'create' && draftMode}">
		<acme:form-double code="auditor.auditReport.form.label.monthsActive" path="monthsActive" readonly="true"/>
		<acme:form-integer code="auditor.auditReport.form.label.hours" path="hours" readonly="true"/>
		<acme:submit code="auditor.auditReport.form.button.update" action="/auditor/auditReport/update"/>
		<acme:submit code="auditor.auditReport.form.button.publish" action="/auditor/auditReport/publish"/>
		<acme:submit code="auditor.auditReport.form.button.delete" action="/auditor/auditReport/delete"/>
	</jstl:if>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="auditor.auditReport.form.button.tactic" action="/auditor/auditSection/list?auditReportId=${id}"/>
	</jstl:if>

	<jstl:if test="${_command == 'create'}">
		<acme:submit code="auditor.auditReport.form.button.create" action="/auditor/auditReport/create"/>
	</jstl:if>
</acme:form>