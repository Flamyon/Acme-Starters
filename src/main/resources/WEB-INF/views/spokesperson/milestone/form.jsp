<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
	<acme:form-textbox code="spokesperson.milestone.form.label.title" path="title" readonly="${!draftMode && id != 0}"/>
	<acme:form-textbox code="spokesperson.milestone.form.label.achievements" path="achievements" readonly="${!draftMode && id != 0}"/>
	<acme:form-double code="spokesperson.milestone.form.label.effort" path="effort" readonly="${!draftMode && id != 0}"/>
	<jstl:if test="${!draftMode && id != 0}"><acme:form-textbox code="spokesperson.milestone.form.label.kind" path="kind" readonly="true"/></jstl:if>
	<jstl:if test="${!(!draftMode && id != 0)}"><acme:form-select code="spokesperson.milestone.form.label.kind" path="kind" choices="${choices}"/></jstl:if>
	<jstl:if test="${_command != 'create' && draftMode}"><acme:submit code="spokesperson.milestone.form.button.update" action="/spokesperson/milestone/update"/><acme:submit code="spokesperson.milestone.form.button.delete" action="/spokesperson/milestone/delete"/></jstl:if>
	<jstl:if test="${_command == 'create'}"><acme:submit code="spokesperson.milestone.form.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/></jstl:if>
</acme:form>
