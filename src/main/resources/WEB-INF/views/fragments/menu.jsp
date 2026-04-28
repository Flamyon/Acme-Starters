<%--
- menu.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/"/>
   			<acme:menu-suboption code="master.menu.anonymous.sponsorships.published" action="/any/sponsorship/list"/>
			<acme:menu-suboption code="master.menu.anonymous.project" action="/any/project/list"/>
			<acme:menu-suboption code="master.menu.anonymous.strategy" action="/any/strategy/list"/>
			<acme:menu-suboption code="master.menu.anonymous.audit-report" action="/any/audit-report/list"/>
			<acme:menu-suboption code="master.menu.anonymous.invention" action="/any/invention/list"/>
			<acme:menu-suboption code="master.menu.anonymous.campaign" action="/any/campaign/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-suboption code="master.menu.administrator.ad-banner.list" action="/administrator/ad-banner/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.sponsorships" access="hasRealm('Sponsor')">
    		<acme:menu-suboption code="master.menu.sponsorships.unpublished" action="/sponsor/sponsorship/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.project.list" action="/manager/project/list"/>
			<acme:menu-suboption code="master.menu.manager.show-dashboard" action="/manager/manager-dashboard/show"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.fundraiser" access="hasRealm('Fundraiser')">
			<acme:menu-suboption code="master.menu.fundraiser.strategy.list" action="/fundraiser/strategy/list"/>
			<acme:menu-suboption code="master.menu.fundraiser.project.list" action="/fundraiser/project/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.inventor" access="hasRealm('Inventor')">
			<acme:menu-suboption code="master.menu.inventor.invention.list" action="/inventor/invention/list"/>
			<acme:menu-suboption code="master.menu.inventor.project.list" action="/inventor/project/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.auditor" access="hasRealm('Auditor')">
			<acme:menu-suboption code="master.menu.auditor.audit-report.list" action="/auditor/audit-report/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.spokesperson" access="hasRealm('Spokesperson')">
			<acme:menu-suboption code="master.menu.spokesperson.campaign.list" action="/spokesperson/campaign/list"/>
			<acme:menu-suboption code="master.menu.spokesperson.project.list" action="/spokesperson/project/list"/>
		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRealm('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.manager-profile" action="/authenticated/manager/update" access="hasRealm('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-sponsor-profile" action="/authenticated/sponsor/create" access="!hasRealm('Sponsor')"/>
			<acme:menu-suboption code="master.menu.user-account.sponsor-profile" action="/authenticated/sponsor/update" access="hasRealm('Sponsor')"/>
			<acme:menu-suboption code="master.menu.user-account.become-fundraiser" action="/authenticated/fundraiser/create" access="!hasRealm('Fundraiser')"/>
			<acme:menu-suboption code="master.menu.user-account.fundraiser-profile" action="/authenticated/fundraiser/update" access="hasRealm('Fundraiser')"/>
			<acme:menu-suboption code="master.menu.user-account.become-auditor" action="/authenticated/auditor/create" access="!hasRealm('Auditor')"/>
			<acme:menu-suboption code="master.menu.user-account.auditor-profile" action="/authenticated/auditor/update" access="hasRealm('Auditor')"/>
			<acme:menu-suboption code="master.menu.user-account.become-inventor" action="/authenticated/inventor/create" access="!hasRealm('Inventor')"/>
			<acme:menu-suboption code="master.menu.user-account.inventor-profile" action="/authenticated/inventor/update" access="hasRealm('Inventor')"/>
			<acme:menu-suboption code="master.menu.user-account.become-spokesperson" action="/authenticated/spokesperson/create" access="!hasRealm('Spokesperson')"/>
			<acme:menu-suboption code="master.menu.user-account.spokesperson-profile" action="/authenticated/spokesperson/update" access="hasRealm('Spokesperson')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>
