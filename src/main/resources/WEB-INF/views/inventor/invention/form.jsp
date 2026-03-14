<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="inventor.invention.form.label.ticker" path="ticker" readonly="${!draftMode}"/>
	<acme:form-textbox code="inventor.invention.form.label.name" path="name" readonly="${!draftMode}"/>
	<acme:form-moment code="inventor.invention.form.label.startMoment" path="startMoment" readonly="${!draftMode}"/>
	<acme:form-moment code="inventor.invention.form.label.endMoment" path="endMoment" readonly="${!draftMode}"/>
	<acme:form-textbox code="inventor.invention.form.label.description" path="description" readonly="${!draftMode}"/>
	<acme:form-url code="inventor.invention.form.label.moreInfo" path="moreInfo" readonly="${!draftMode}"/>
	
	<jstl:if test="${_command != 'create' && draftMode}">
		<acme:form-textbox code="inventor.invention.form.label.monthsActive" path="monthsActive" readonly="true"/>
		<acme:form-money code="inventor.invention.form.label.cost" path="cost" readonly="true"/>
		<acme:submit code="inventor.invention.form.button.update" action="/inventor/invention/update"/>
		<acme:submit code="inventor.invention.form.button.publish" action="/inventor/invention/publish"/>
		<acme:submit code="inventor.invention.form.button.delete" action="/inventor/invention/delete"/>
	</jstl:if>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="inventor.invention.form.button.part" action="/inventor/part/list?inventionId=${id}"/>
	</jstl:if>

	<jstl:if test="${_command == 'create'}">
		<acme:submit code="inventor.invention.form.button.create" action="/inventor/invention/create"/>
	</jstl:if>
</acme:form>
