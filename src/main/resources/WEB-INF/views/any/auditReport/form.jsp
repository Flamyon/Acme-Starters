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
	<acme:form-textbox code="any.auditReport.form.label.ticker" path="ticker" readonly="true"/>
	<acme:form-textbox code="any.auditReport.form.label.name" path="name" readonly="true"/>
	<acme:form-textbox code="any.auditReport.form.label.description" path="description" readonly="true"/>
	<acme:form-moment code="any.auditReport.form.label.startMoment" path="startMoment" readonly="true"/>
	<acme:form-moment code="any.auditReport.form.label.endMoment" path="endMoment" readonly="true"/>
	<acme:form-url code="any.auditReport.form.label.moreInfo" path="moreInfo" readonly="true"/>
	<acme:form-double code="any.auditReport.form.label.monthsActive" path="monthsActive" readonly="true"/>
	<acme:form-integer code="any.auditReport.form.label.hours" path="hours" readonly="true"/>

	<jstl:if test="${_command == 'show'}">
		<acme:button code="any.auditReport.form.button.auditor" action="/any/auditor/show?auditReportId=${id}"/>
		<acme:button code="any.auditReport.form.button.auditSection" action="/any/auditSection/list?auditReportId=${id}"/>
	</jstl:if>
</acme:form>