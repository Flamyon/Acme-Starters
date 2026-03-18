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

<%--
- form.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.milestone.form.label.title" path="title" readonly="true"/>
	<acme:form-textbox code="any.milestone.form.label.achievements" path="achievements" readonly="true"/>
	<acme:form-textbox code="any.milestone.form.label.effort" path="effort" readonly="true"/>
	<acme:form-textbox code="any.milestone.form.label.kind" path="kind" readonly="true"/>
	<%-- Botón para volver a la campaña --%>
	<acme:button code="any.milestone.form.button.campaign" action="/any/campaign/show?id=${campaignId}"/>
</acme:form>