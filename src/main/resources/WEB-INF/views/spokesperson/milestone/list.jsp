<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:list>
	<acme:list-column code="spokesperson.milestone.list.label.title" path="title" width="40%"/>
	<acme:list-column code="spokesperson.milestone.list.label.effort" path="effort" width="30%"/>
	<acme:list-column code="spokesperson.milestone.list.label.kind" path="kind" width="30%"/>
</acme:list>
<acme:button code="spokesperson.milestone.form.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/>
