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
	<acme:form-textbox code="auditor.auditSection.form.label.name" path="name" readonly="${!draftMode && id != 0}"/>
	<acme:form-textbox code="auditor.auditSection.form.label.notes" path="notes" readonly="${!draftMode && id != 0}"/>
	<acme:form-integer code="auditor.auditSection.form.label.hours" path="hours" readonly="${!draftMode && id != 0}"/>


	<jstl:if test="${!draftMode && id != 0}">
		<acme:form-textbox code="any.auditSection.form.label.kind" path="kind" readonly = "true"/>
	</jstl:if>
	
	<jstl:if test="${!(!draftMode && id != 0)}">
		<acme:form-select code="auditor.auditSection.form.label.kind" path="kind" choices="${choices}"/>	
	</jstl:if>
	
	<jstl:if test="${_command != 'create' && draftMode}">
		<acme:submit code="auditor.auditSection.form.button.update" action="/auditor/auditSection/update"/>
		<acme:submit code="auditor.auditSection.form.button.delete" action="/auditor/auditSection/delete"/>
	</jstl:if>

	<jstl:if test="${_command == 'create'}">
		<acme:submit code="auditor.auditSection.form.button.create" action="/auditor/auditSection/create?auditReportId=${auditReportId}"/>
	</jstl:if>
</acme:form>