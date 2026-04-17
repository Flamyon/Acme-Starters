<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:list>
	<acme:list-column code="spokesperson.campaign.list.label.ticker" path="ticker" width="18%"/>
	<acme:list-column code="spokesperson.campaign.list.label.name" path="name" width="28%"/>
	<acme:list-column code="spokesperson.campaign.form.label.startMoment" path="startMoment" width="27%"/>
	<acme:list-column code="spokesperson.campaign.list.label.project" path="project.title" width="27%"/>
</acme:list>
<acme:button code="spokesperson.campaign.form.button.create" action="/spokesperson/campaign/create"/>
