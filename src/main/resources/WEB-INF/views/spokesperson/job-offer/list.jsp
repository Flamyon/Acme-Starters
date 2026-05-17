<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-striped table-condensed table-hover">
	<thead>
		<tr>
			<th><acme:print code="jobOffer.title"/></th>
			<th><acme:print code="jobOffer.company"/></th>
			<th><acme:print code="jobOffer.description"/></th>
			<th><acme:print code="jobOffer.apply"/></th>
		</tr>
	</thead>
	<tbody>
		<jstl:if test="${$list$number$data != null && $list$number$data >= 1}">
			<jstl:forEach var="index" begin="${0}" end="${$list$number$data - 1}">
				<jstl:set var="index_title" value="title[${index}]"/>
				<jstl:set var="index_company" value="companyName[${index}]"/>
				<jstl:set var="index_desc" value="description[${index}]"/>
				<jstl:set var="index_url" value="url[${index}]"/>

				<tr>
					<td><acme:print value="${requestScope[index_title]}"/></td>
					<td><acme:print value="${requestScope[index_company]}"/></td>
					<td><acme:print value="${requestScope[index_desc]}"/></td>
					<td>
						<a href="${requestScope[index_url]}" target="_blank" rel="noopener noreferrer">
							<acme:print code="jobOffer.apply"/>
						</a>
					</td>
				</tr>
			</jstl:forEach>
		</jstl:if>
	</tbody>
</table>

<acme:return/>
