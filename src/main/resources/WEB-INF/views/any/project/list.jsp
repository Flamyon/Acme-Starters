<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.project.list.label.title" path="title" width="30%"/>
	<acme:list-column code="any.project.list.label.kickOff" path="kickOff" width="20%"/>
	<acme:list-column code="any.project.list.label.closeOut" path="closeOut" width="20%"/>
	<acme:list-column code="any.project.list.label.manager" path="manager.identity.fullName" width="30%"/>
</acme:list>
