<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.project-member.list.label.fullName" path="memberFullName" width="45%"/>
	<acme:list-column code="manager.project-member.list.label.email" path="memberEmail" width="35%"/>
	<acme:list-column code="manager.project-member.list.label.role" path="roleLabel" width="20%"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="manager.project-member.list.button.create" action="/manager/project-member/create?projectId=${projectId}"/>
</jstl:if>
<acme:button code="manager.project-member.list.button.return" action="/manager/project/show?id=${projectId}"/>
