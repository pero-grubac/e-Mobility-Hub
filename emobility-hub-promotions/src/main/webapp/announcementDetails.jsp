<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="userBean"
	class="org.unibl.etf.emobility_hub_promotions.models.beans.UserBean"
	scope="session" />
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.services.AnnouncementService"%>
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.models.beans.AnnouncementResponseBean"%>
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.models.dto.request.AnnouncementRequest"%>
<%@ page import="java.time.format.DateTimeFormatter"%>

<%
if (userBean == null || !userBean.isLoggedIn() || userBean.getToken() == null || userBean.getToken().isEmpty()) {
	response.sendRedirect("login.jsp");
	return;
}

String action = request.getParameter("action");
String errorMessage = null;
AnnouncementResponseBean announcement = null;

try {
	AnnouncementService announcementService = new AnnouncementService();

	if (action != null) {
		if ("update".equals(action)) {
	AnnouncementRequest announcementRequest = new AnnouncementRequest();
	announcementRequest.setId(Long.parseLong(request.getParameter("id")));
	announcementRequest.setTitle(request.getParameter("title"));
	announcementRequest.setContent(request.getParameter("content"));

	announcementService.update(userBean.getToken(), announcementRequest.getId(), announcementRequest);
	response.sendRedirect("announcementDetails.jsp?id=" + announcementRequest.getId());

	return;
		} else if ("delete".equals(action)) {
	Long id = Long.parseLong(request.getParameter("id"));
	announcementService.delete(userBean.getToken(), id);
	response.sendRedirect("announcements.jsp");
	return;
		}
	} else {
		String promotionId = request.getParameter("id");
		announcement = announcementService.getById(userBean.getToken(), Long.parseLong(promotionId));
	}
} catch (Exception e) {
	errorMessage = "Error: " + e.getMessage();
	//session.invalidate();
	//response.sendRedirect("login.jsp");
	return;
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Promotion Details</title>
<!-- Bootstrap 3 CSS -->
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

.form-group {
	margin-bottom: 20px;
}

.button-container {
	display: flex;
	justify-content: space-between;
}

.button-container .btn {
	margin-right: 10px;
}

.navigation-buttons {
	margin-top: 20px;
}

h1 {
	text-align: center;
	margin-bottom: 30px;
}
</style>
</head>
<body class="bg-light">
	<div class="container mt-5">
		<form action="logout.jsp" method="post" style="display: inline;">
			<button type="submit" class="btn btn-danger logout-btn">Logout</button>
		</form>

		<h1>Announcement Details</h1>

		<%
		if (errorMessage != null) {
		%>
		<div class="alert alert-danger"><%=errorMessage%></div>
		<%
		} else if (announcement != null) {
		%>
		<form method="post">
			<div class="card mt-4 shadow-sm">
				<div class="card-body">
					<div class="form-group">
						<label for="title" class="form-label">Title</label> <input
							type="text" class="form-control" id="title" name="title"
							value="<%=announcement.getTitle()%>" required>
					</div>
					<div class="form-group">
						<label for="content" class="form-label">Content</label>
						<textarea class="form-control" id="content" name="content"
							rows="3" required><%=announcement.getContent()%></textarea>
					</div>

				</div>
				<div class="card-footer">
					<div class="button-container">
						<button type="submit" name="action" value="update"
							class="btn btn-primary">Update</button>
						<button type="submit" name="action" value="delete"
							class="btn btn-danger"
							onclick="return confirm('Are you sure you want to delete this announcement?');">Delete</button>
					</div>
				</div>
			</div>
		</form>

		<!-- Back to Promotions Button -->
		<div class="navigation-buttons mt-4">
			<button class="btn btn-secondary" onclick="redirectToAnnouncements()">Back
				to Announcements</button>
		</div>
	</div>
	<%
	} else {
	%>
	<div class="alert alert-warning">Promotion not found.</div>
	<%
	}
	%>
	</div>


	<script>
		function redirectToAnnouncements() {
			window.location.href = "announcements.jsp";
		}
	</script>
</body>
</html>


