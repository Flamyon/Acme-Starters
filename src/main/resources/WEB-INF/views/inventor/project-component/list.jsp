<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="inventor.project-components.list.label.kind" path="kindLabel" width="14%"/>
	<acme:list-column code="inventor.project-components.list.label.ticker" path="ticker" width="18%"/>
	<acme:list-column code="inventor.project-components.list.label.name" path="name" width="28%"/>
	<acme:list-column code="inventor.project-components.list.label.owner" path="owner" width="20%"/>
	<acme:list-column code="inventor.project-components.list.label.draft" path="draftMode" width="10%"/>
	<acme:list-column code="inventor.project-components.list.label.start" path="startMoment" width="10%"/>
</acme:list>

<acme:button code="inventor.project-components.list.button.return" action="/inventor/project/show?id=${projectId}"/>