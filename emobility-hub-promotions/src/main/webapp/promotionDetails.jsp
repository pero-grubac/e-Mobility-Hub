<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="userBean"
	class="org.unibl.etf.emobility_hub_promotions.models.beans.UserBean"
	scope="session" />
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.services.PromotionService"%>
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.models.beans.PromotionResponseBean"%>
<%@ page
	import="org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest"%>
<%@ page import="java.time.format.DateTimeFormatter"%>

<%
if (userBean == null || !userBean.isLoggedIn() || userBean.getToken() == null || userBean.getToken().isEmpty()) {
	response.sendRedirect("login.jsp");
	return;
}

String action = request.getParameter("action");
String errorMessage = null;
PromotionResponseBean promotion = null;

try {
	PromotionService promotionService = new PromotionService();

	if (action != null) {
		if ("update".equals(action)) {
	PromotionRequest promotionRequest = new PromotionRequest();
	promotionRequest.setId(Long.parseLong(request.getParameter("id")));
	promotionRequest.setTitle(request.getParameter("title"));
	promotionRequest.setContent(request.getParameter("content"));
	promotionRequest.setStartDate(request.getParameter("startDate").replace(" ", "T"));
	promotionRequest.setEndDate(request.getParameter("endDate").replace(" ", "T"));

	promotionService.updatePromotion(userBean.getToken(), promotionRequest.getId(), promotionRequest);
	response.sendRedirect("promotionDetails.jsp?id=" + promotionRequest.getId());
	return;
		} else if ("delete".equals(action)) {
	Long id = Long.parseLong(request.getParameter("id"));
	promotionService.deletePromotion(userBean.getToken(), id);
	response.sendRedirect("promotions.jsp");
	return;
		}
	} else {
		String promotionId = request.getParameter("id");
		promotion = promotionService.getPromotionById(userBean.getToken(), Long.parseLong(promotionId));
	}
} catch (Exception e) {
	errorMessage = "Error: " + e.getMessage();
	session.invalidate();
	response.sendRedirect("login.jsp");
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
		<!-- Logout Button -->
		<form action="logout.jsp" method="post" style="display: inline;">
			<button type="submit" class="btn btn-danger logout-btn">Logout</button>
		</form>

		<!-- Promotion Details Header -->
		<h1>Promotion Details</h1>

		<%
		if (errorMessage != null) {
		%>
		<div class="alert alert-danger"><%=errorMessage%></div>
		<%
		} else if (promotion != null) {
		%>
		<form method="post">
			<div class="card mt-4 shadow-sm">
				<div class="card-body">
					<div class="form-group">
						<label for="title" class="form-label">Title</label> <input
							type="text" class="form-control" id="title" name="title"
							value="<%=promotion.getTitle()%>" required>
					</div>
					<div class="form-group">
						<label for="content" class="form-label">Content</label>
						<textarea class="form-control" id="content" name="content"
							rows="3" required><%=promotion.getContent()%></textarea>
					</div>
					<div class="form-group">
						<label for="startDate" class="form-label">Start Date</label>
						<div class='input-group date' id='startDatePicker'>
							<input type='text' class="form-control" id="startDate"
								name="startDate"
								value="<%=promotion.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))%>"
								required /> <span class="input-group-addon"> <span
								class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
					<div class="form-group">
						<label for="endDate" class="form-label">End Date</label>
						<div class='input-group date' id='endDatePicker'>
							<input type='text' class="form-control" id="endDate"
								name="endDate"
								value="<%=promotion.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))%>"
								required /> <span class="input-group-addon"> <span
								class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="card-footer">
					<div class="button-container">
						<button type="submit" name="action" value="update"
							class="btn btn-primary">Update</button>
						<button type="submit" name="action" value="delete"
							class="btn btn-danger"
							onclick="return confirm('Are you sure you want to delete this promotion?');">Delete</button>
					</div>
				</div>
			</div>
		</form>

		<!-- Back to Promotions Button -->
		<div class="navigation-buttons mt-4">
			<button class="btn btn-secondary" onclick="redirectToPromotions()">Back
				to Promotions</button>
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

	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<!-- Moment.js -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
	<!-- Bootstrap 3 JS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<!-- Datetimepicker JS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>

	<script>
		$(document).ready(function() {
			$('#startDatePicker').datetimepicker({
				format : 'YYYY-MM-DD HH:mm',
			});
			$('#endDatePicker').datetimepicker({
				format : 'YYYY-MM-DD HH:mm',
			});
		});
	</script>

	<script>
		function redirectToPromotions() {
			window.location.href = "promotions.jsp";
		}
	</script>
</body>
</html>


