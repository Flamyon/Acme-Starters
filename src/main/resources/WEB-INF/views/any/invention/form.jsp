<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.invention.form.label.ticker" path="ticker" readonly="true"/>
	<acme:form-textbox code="any.invention.form.label.name" path="name" readonly="true"/>
	<acme:form-moment code="any.invention.form.label.startMoment" path="startMoment" readonly="true"/>
	<acme:form-moment code="any.invention.form.label.endMoment" path="endMoment" readonly="true"/>
	<acme:form-textbox code="any.invention.form.label.description" path="description" readonly="true"/>
	<acme:form-url code="any.invention.form.label.moreInfo" path="moreInfo" readonly="true"/>
	<acme:form-double code="any.invention.form.label.monthsActive" path="monthsActive" readonly="true"/>
	<acme:form-money code="any.invention.form.label.cost" path="cost" readonly="true"/>

	<jstl:if test="${_command == 'show'}">
		<acme:button code="any.invention.form.button.inventor" action="/any/inventor/show?inventionId=${id}"/>
		<acme:button code="any.invention.form.button.part" action="/any/part/list?inventionId=${id}"/>
	</jstl:if>
</acme:form>
