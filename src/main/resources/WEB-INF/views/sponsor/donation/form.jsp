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
<<<<<<< HEAD

=======
>>>>>>> 05ee1e65c64b3a95b052a8c1b0a8d2108cd84705
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
<<<<<<< HEAD
	<acme:form-textbox code="sponsor.donation.form.label.name" path="name"/>
	<acme:form-money code="sponsor.donation.form.label.money" path="money"/>	
	<acme:form-textbox code="sponsor.donation.form.label.kind" path="kind"/>
	<acme:form-textarea code="sponsor.donation.form.label.notes" path="notes"/>
=======
    <acme:form-textbox code="sponsor.donation.form.label.name" path="name"/>
    <acme:form-money code="sponsor.donation.form.label.money" path="money"/>
    <acme:form-select code="sponsor.donation.form.label.kind" path="kind" choices="${kinds}"/>
    <acme:form-textarea code="sponsor.donation.form.label.notes" path="notes"/>

    <acme:button code="sponsor.donation.form.button.return" 
                 action="/sponsor/sponsorship/show?id=${sponsorshipId}"/>

    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
            <acme:submit code="sponsor.donation.form.button.update" action="/sponsor/donation/update"/>
            <acme:submit code="sponsor.donation.form.button.delete" action="/sponsor/donation/delete"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="sponsor.donation.form.button.create" 
                         action="/sponsor/donation/create?sponsorshipId=${sponsorshipId}"/>
        </jstl:when>
    </jstl:choose>
>>>>>>> 05ee1e65c64b3a95b052a8c1b0a8d2108cd84705
</acme:form>