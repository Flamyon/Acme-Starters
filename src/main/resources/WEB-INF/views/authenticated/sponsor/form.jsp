<%--
- form.jsp
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
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
<<<<<<< HEAD:src/main/resources/WEB-INF/views/sponsor/sponsor/form.jsp
	<acme:form-textbox code="sponsor.sponsor.form.label.name" path="identity.name"/>
	<acme:form-textbox code="sponsor.sponsor.form.label.surname" path="identity.surname"/>
	<acme:form-textbox code="sponsor.sponsor.form.label.email" path="identity.email"/>
	<acme:form-textbox code="sponsor.sponsor.form.label.address" path="address"/>
	<acme:form-textbox code="sponsor.sponsor.form.label.im" path="im"/>
	<acme:form-textbox code="sponsor.sponsor.form.label.gold" path="gold"/>
=======
	<acme:form-textbox code="sponsor.sponsor.form.label.address" path="address"/>
	<acme:form-textbox code="sponsor.sponsor.form.label.im" path="im"/>
	<acme:form-select code="sponsor.sponsor.form.label.gold" path="gold" choices="${goldChoices}"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.sponsor.form.button.create" action="/authenticated/sponsor/create"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.sponsor.form.button.update" action="/authenticated/sponsor/update"/>
	</jstl:if>
>>>>>>> 05ee1e65c64b3a95b052a8c1b0a8d2108cd84705:src/main/resources/WEB-INF/views/authenticated/sponsor/form.jsp
</acme:form>