<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="inventor.part.list.label.name" path="name" width="40%"/>	
	<acme:list-column code="inventor.part.list.label.cost" path="cost" width="30%"/>
	<acme:list-column code="inventor.part.list.label.kind" path="kind" width="30%"/>
</acme:list>
		<acme:button code="inventor.part.form.button.create" action="/inventor/part/create?inventionId=${inventionId}"/>
