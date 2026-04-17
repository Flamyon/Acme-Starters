<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.project.list.label.title" path="title" width="34%"/>
	<acme:list-column code="manager.project.list.label.kickOff" path="kickOff" width="22%"/>
	<acme:list-column code="manager.project.list.label.closeOut" path="closeOut" width="22%"/>
	<acme:list-column code="manager.project.list.label.draftMode" path="draftMode" width="22%"/>
</acme:list>

<acme:button code="manager.project.list.button.create" action="/manager/project/create"/>
