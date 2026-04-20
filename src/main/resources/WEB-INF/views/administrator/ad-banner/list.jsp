<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.ad-banner.list.label.slogan" path="slogan" width="35%"/>
	<acme:list-column code="administrator.ad-banner.list.label.target-url" path="targetUrl" width="35%"/>
	<acme:list-column code="administrator.ad-banner.list.label.picture" path="picture" width="30%"/>
</acme:list>

<acme:button code="administrator.ad-banner.list.button.create" action="/administrator/ad-banner/create"/>
