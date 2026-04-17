<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="manager.manager-dashboard.form.title.general"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.manager-dashboard.form.label.total-projects"/>
		</th>
		<td>
			<acme:print value="${totalProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.manager-dashboard.form.label.deviation-from-other-managers-average"/>
		</th>
		<td>
			<acme:print value="${deviationFromOtherManagersAverage}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="manager.manager-dashboard.form.title.project-effort"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.manager-dashboard.form.label.min-project-effort"/>
		</th>
		<td>
			<acme:print value="${minProjectEffort}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.manager-dashboard.form.label.max-project-effort"/>
		</th>
		<td>
			<acme:print value="${maxProjectEffort}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.manager-dashboard.form.label.avg-project-effort"/>
		</th>
		<td>
			<acme:print value="${avgProjectEffort}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.manager-dashboard.form.label.dev-project-effort"/>
		</th>
		<td>
			<acme:print value="${devProjectEffort}"/>
		</td>
	</tr>
</table>

<acme:return/>
