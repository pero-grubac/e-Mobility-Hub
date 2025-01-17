<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="userBean"
	class="org.unibl.etf.emobility_hub_promotions.models.beans.UserBean"
	scope="session" />
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.services.AnnouncementService"%>
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.models.dto.request.AnnouncementRequest"%>

<%
if (userBean == null || !userBean.isLoggedIn() || userBean.getToken() == null || userBean.getToken().isEmpty()) {
	response.sendRedirect("login.jsp");
	return;
}

String errorMessage = null;

try {
	if ("create".equals(request.getParameter("action"))) {
		AnnouncementRequest announcementRequest = new AnnouncementRequest();
		announcementRequest.setTitle(request.getParameter("title"));
		announcementRequest.setContent(request.getParameter("content"));

		AnnouncementService announcementService = new AnnouncementService();
		announcementService.create(userBean.getToken(), announcementRequest);

		response.sendRedirect("announcements.jsp");
		return;
	}
} catch (Exception e) {
	errorMessage = "Error: " + e.getMessage();
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Create Announcement</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<!-- Datetimepicker CSS -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css">
<style>
.logout-btn {
	position: absolute;
	top: 20px;
	right: 20px;
}

h1 {
	text-align: center;
	margin-bottom: 30px;
}

.navigation-buttons {
	margin-top: 20px;
}
</style>
</head>
<body class="bg-light">
	<div class="container mt-5">
		<form action="logout.jsp" method="post" style="display: inline;">
			<button type="submit" class="btn btn-danger logout-btn">Logout</button>
		</form>

		<h1>Create Announcement</h1>

		<%
		if (errorMessage != null) {
		%>
		<div class="alert alert-danger"><%=errorMessage%></div>
		<%
		}
		%>

		<form method="post">
			<div class="card mt-4 shadow-sm">
				<div class="card-body">
					<div class="form-group">
						<label for="title" class="form-label">Title</label> <input
							type="text" class="form-control" id="title" name="title" required>
					</div>
					<div class="form-group">
						<label for="content" class="form-label">Content</label>
						<textarea class="form-control" id="content" name="content"
							rows="3" required></textarea>
					</div>

				</div>
				<div class="card-footer d-flex justify-content-between">
					<button type="submit" name="action" value="create"
						class="btn btn-primary">Create</button>
					<button type="button" class="btn btn-secondary"
						onclick="redirectToAnnouncements()">Back to Announcements</button>
				</div>
			</div>
		</form>
	</div>
	<script>
		function redirectToAnnouncements() {
			window.location.href = "announcements.jsp";
		}
	</script>
</body>
</html>
