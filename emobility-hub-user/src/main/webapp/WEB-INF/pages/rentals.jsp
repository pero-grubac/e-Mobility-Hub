<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="org.unibl.etf.emobility_hub_user.models.entity.RentalEntity"%>
<%@ page
	import="org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse"%>
<jsp:useBean id="rentalBean"
	class="org.unibl.etf.emobility_hub_user.beans.RentalBean"
	scope="session" />
<jsp:useBean id="bicycleBean"
	class="org.unibl.etf.emobility_hub_user.beans.ElectricBicycleBean"
	scope="session" />
<jsp:useBean id="transportVehicleBean"
	class="org.unibl.etf.emobility_hub_user.beans.TransportVehicleBean" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Rentals</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
.card {
	margin-bottom: 15px;
}

.card img {
	max-width: 100%;
	height: 200px;
	object-fit: cover;
}

.pagination {
	margin-top: 20px;
	justify-content: center;
}
</style>
</head>
<body>
	<jsp:include page="header.html" />

	<div class="container mt-5">

		<h2 class="mb-4">Rentals</h2>

		<div class="row">
			<%
			PaginatedResponse<RentalEntity> rentals = rentalBean.getRentals();
			if (rentals != null && rentals.getContent() != null) {
				for (RentalEntity rental : rentals.getContent()) {
					String imagePath = transportVehicleBean.imageById(rental.getVehicleId());
					if (imagePath == null || imagePath.isEmpty()) {
				imagePath = request.getContextPath() + "/images/default-avatar.png";
					}
			%>
			<div class="col-md-4">
				<div class="card">
					<img src="<%=imagePath%>" alt="Vehicle Image" class="card-img-top">
					<div class="card-body">
						<h5 class="card-title">
							Rental ID:
							<%=rental.getId()%></h5>
						<p class="card-text">
							Duration:
							<%=String.format("%.2f", rental.getDuration())%>
							hours<br> Distance:
							<%=String.format("%.2f", rental.getDistance())%>
							km<br> Price: $<%=String.format("%.2f", rental.getPrice())%><br>
							Start:
							<%=rental.getRentalStart().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))%><br>
							End:
							<%=rental.getRentalEnd().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))%>
						</p>
					</div>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<div class="col-12">
				<p>No rentals available.</p>
			</div>
			<%
			}
			%>
		</div>

		<!-- Pagination -->
		<nav>
			<ul class="pagination">
				<%
				int totalPages = rentals.getPage().getTotalPages();
				int currentPage = rentals.getPage().getNumber();
				for (int i = 1; i <= totalPages; i++) {
				%>
				<li class="page-item <%=(i == currentPage) ? "active" : ""%>">
					<a class="page-link" href="clients?action=rentals&page=<%=i%>"><%=i%></a>
				</li>
				<%
				}
				%>
			</ul>
		</nav>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
