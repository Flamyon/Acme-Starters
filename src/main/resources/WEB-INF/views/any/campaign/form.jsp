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
	<acme:form-textbox code="any.campaign.form.label.ticker" path="ticker" readonly="true"/>
	<acme:form-textbox code="any.campaign.form.label.name" path="name" readonly="true"/>
	<acme:form-textarea code="any.campaign.form.label.description" path="description" readonly="true"/>
	<acme:form-moment code="any.campaign.form.label.startMoment" path="startMoment" readonly="true"/>
	<acme:form-moment code="any.campaign.form.label.endMoment" path="endMoment" readonly="true"/>
	<acme:form-url code="any.campaign.form.label.moreInfo" path="moreInfo" readonly="true"/>
	<acme:form-textbox code="any.campaign.form.label.monthsActive" path="monthsActive" readonly="true"/>
	<acme:form-textbox code="any.campaign.form.label.effort" path="effort" readonly="true"/>
	<acme:button code="any.campaign.form.button.milestones" action="/any/milestone/list?campaignId=${id}"/>
	<acme:button code="any.campaign.form.button.spokesperson" action="/any/spokesperson/show?id=${spokespersonId}"/>
</acme:form>