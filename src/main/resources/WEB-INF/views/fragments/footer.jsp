<%--
- footer.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:footer-panel>
	<acme:footer-subpanel code="master.footer.title.about">
		<acme:footer-option icon="fa fa-building" code="master.footer.label.company" action="/any/system/company"/>
		<acme:footer-option icon="fa fa-file" code="master.footer.label.license" action="/any/system/license"/>
	</acme:footer-subpanel>

	<acme:footer-subpanel code="master.footer.title.social">
		<acme:print var="$linkedin$url" code="master.footer.url.linkedin"/>
		<acme:footer-option icon="fab fa-linkedin" code="master.footer.label.linked-in" action="${$linkedin$url}" newTab="true"/>
		<acme:print var="$twitter$url" code="master.footer.url.twitter"/>
		<acme:footer-option icon="fab fa-twitter" code="master.footer.label.twitter" action="${$twitter$url}" newTab="true"/>
	</acme:footer-subpanel>

	<acme:footer-subpanel code="master.footer.title.languages">
		<acme:footer-option icon="fa fa-language" code="master.footer.label.english" action="/?locale=en"/>
		<acme:footer-option icon="fa fa-language" code="master.footer.label.spanish" action="/?locale=es"/>
	</acme:footer-subpanel>

	<acme:footer-logo logo="images/logo.png" alt="master.company.name">
		<acme:footer-copyright code="master.company.name"/>
	</acme:footer-logo>
</acme:footer-panel>

<jstl:if test="${adBanner != null}">
	<div id="ad-banner-slot" class="text-center" style="margin: 1rem 0 0 0;">
		<small class="text-muted"><acme:print code="master.footer.label.sponsored"/></small>
		<div>
			<a href="${adBanner.targetUrl}" target="_blank" rel="noopener noreferrer">
				<img src="${adBanner.picture}" alt="sponsored-banner" class="img-fluid rounded" style="max-height: 64px; margin-top: .35rem; border: 1px solid #d9d9d9;"/>
			</a>
		</div>
		<div style="margin-top: .25rem;">
			<a href="${adBanner.targetUrl}" target="_blank" rel="noopener noreferrer" style="font-size: 0.9rem;">
				<jstl:out value="${adBanner.slogan}"/>
			</a>
		</div>
	</div>
</jstl:if>
