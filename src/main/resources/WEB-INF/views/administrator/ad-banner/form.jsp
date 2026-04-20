<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="administrator.ad-banner.form.label.slogan" path="slogan"/>
	<acme:form-url code="administrator.ad-banner.form.label.target-url" path="targetUrl"/>
	<acme:form-url code="administrator.ad-banner.form.label.picture" path="picture"/>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="administrator.ad-banner.form.button.update" action="/administrator/ad-banner/update"/>
			<acme:submit code="administrator.ad-banner.form.button.delete" action="/administrator/ad-banner/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.ad-banner.form.button.create" action="/administrator/ad-banner/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
