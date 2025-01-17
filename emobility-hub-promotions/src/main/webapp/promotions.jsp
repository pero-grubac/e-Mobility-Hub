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
	import="org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Promotions</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/bootstrap.min.css">
</head>
<body class="bg-light">
	<%
	// Provera sesije korisnika
	if (userBean == null || !userBean.isLoggedIn() || userBean.getToken() == null || userBean.getToken().isEmpty()) {
		response.sendRedirect("login.jsp");
		return;
	}
	String searchQuery = request.getParameter("search");
	if (searchQuery != null) {
	    searchQuery = searchQuery.trim();
	    if (searchQuery.isEmpty()) {
	        searchQuery = null; 
	    }
	}
	int pageNum = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 0;
	int size = 6;
	List<PromotionResponseBean> promotions = null;
	int totalPages = 0;
	String errorMessage = null;

	try {
		PromotionService promotionService = new PromotionService();
		PaginatedResponse<PromotionResponseBean> paginatedResponse = promotionService.getPromotions(userBean.getToken(),
		pageNum, size, searchQuery);

		if (paginatedResponse != null) {
			promotions = paginatedResponse.getContent();
			totalPages = paginatedResponse.getPage().getTotalPages();
		}
	} catch (Exception e) {
		session.invalidate();
		response.sendRedirect("login.jsp");
		return;
	}
	%>

	<div class="container mt-5">
		<div class="d-flex justify-content-between align-items-center">
			<h1>Promotions</h1>
			<div>
				<button class="btn btn-info" onclick="redirectToAnnouncements()">Announcements</button>

				<button class="btn btn-primary" onclick="redirectToCreate()">Create Promotion</button>

				<form action="logout.jsp" method="post" style="display: inline;">
					<button type="submit" class="btn btn-danger">Logout</button>
				</form>
			</div>
		</div>

		<form method="get" class="mt-3" id="searchForm">
			<div class="input-group">
				<input type="text" class="form-control" name="search"
					placeholder="Search by content"
					value="<%=request.getParameter("search") != null ? request.getParameter("search") : ""%>">
				<div class="input-group-append">
					<button class="btn btn-outline-primary" type="submit">Search</button>
				</div>
			</div>
		</form>

		<%
		if (errorMessage != null) {
		%>
		<div class="alert alert-danger"><%=errorMessage%></div>
		<%
		}
		%>

		<div class="row mt-4">
			<%
			if (promotions != null && !promotions.isEmpty()) {
			%>
			<%
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			for (PromotionResponseBean promotion : promotions) {
			%>
			<div class="col-md-4 mb-3">
				<div class="card">
					<div class="card-body">
						<h5 class="card-title"><%=promotion.getTitle()%></h5>
						<p class="card-text">
							<%
							String shortContent = promotion.getContent().length() > 30
									? promotion.getContent().substring(0, 30) + "..."
									: promotion.getContent();
							%>
							<%=shortContent%>
						</p>
						<p class="text-muted">
							From:
							<%=promotion.getStartDate().format(formatter)%>
						</p>
						<p class="text-muted">
							To:
							<%=promotion.getEndDate().format(formatter)%>
						</p>

						<button class="btn btn-primary"
							onclick="redirectToDetails('<%=promotion.getId()%>')">Details</button>
					</div>
				</div>
			</div>
			<%
			}
			%>
			<%
			} else {
			%>
			<div class="alert alert-warning">No promotions found.</div>
			<%
			}
			%>
		</div>

		<nav aria-label="Page navigation">
			<ul class="pagination justify-content-center">
				<%
				for (int i = 0; i < totalPages; i++) {
				%>
				<li class="page-item <%=(i == pageNum) ? "active" : ""%>"><a
					class="page-link" href="promotions.jsp?page=<%=i%>&size=<%=size%>"><%=i + 1%></a>
				</li>
				<%
				}
				%>
			</ul>
		</nav>
	</div>

	<script>
		function redirectToDetails(id) {
			window.location.href = "promotionDetails.jsp?id=" + id;
		}
	</script>
	<script>
		function redirectToCreate() {
			window.location.href = "createPromotion.jsp";
		}
	</script>
	<script>
		function redirectToAnnouncements() {
			window.location.href = "announcements.jsp";
		}
	</script>
	<script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
</body>
</html>
