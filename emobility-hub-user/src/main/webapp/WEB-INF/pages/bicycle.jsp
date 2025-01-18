<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="org.unibl.etf.emobility_hub_user.models.entity.ElectricBicycleEntity"%>
<%@ page import="java.util.List"%>
<%@ page
	import="org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse"%>
<jsp:useBean id="bicycleBean"
	class="org.unibl.etf.emobility_hub_user.beans.ElectricBicycleBean"
	scope="session" />

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Bicycles</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
.card-img-top {
	width: 100%;
	height: 200px;
	object-fit: cover;
}
</style>
</head>
<body>
	<jsp:include page="header.html" />
	<div class="container mt-5">

		<h2 class="mb-4">Electric Bicycles</h2>

		<div class="row">
			<%
			PaginatedResponse<ElectricBicycleEntity> bicycles = bicycleBean.getElectricBicycles();
			if (bicycles != null && bicycles.getContent() != null) {
				for (ElectricBicycleEntity bicycle : bicycles.getContent()) {
					String imagePath = (bicycle.getImage() != null && !bicycle.getImage().isEmpty())
					? bicycle.getImage()
					: request.getContextPath() + "/images/default-avatar.png";
			%>
			<div class="col-md-4 mb-4">
				<div class="card">
					<img src="<%=imagePath%>" class="card-img-top" alt="Bicycle Image">
					<div class="card-body">
						<h5 class="card-title"><%=bicycle.getModel()%></h5>
						<p class="card-text">
							$<%=bicycle.getRentPrice()%></p>
						<div class="d-flex justify-content-between">
							<button class="btn btn-success"
								onclick="openRentModal('<%=bicycle.getId()%>')">Rent</button>

							<button class="btn btn-warning"
								onclick="openBrokenModal('<%=bicycle.getId()%>')">
								Broken</button>
						</div>
					</div>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<div class="col-12">
				<p>No bicycles available.</p>
			</div>
			<%
			}
			%>
		</div>

		<!-- Pagination -->
		<nav aria-label="Page navigation">
			<ul class="pagination justify-content-center">
				<%
				int totalPages = bicycles.getPage().getTotalPages();
				int currentPage = (session.getAttribute("currentPage") != null) ? (Integer) session.getAttribute("currentPage") : 1;
				int visiblePages = 3; // Broj stranica prikazanih sa svake strane trenutne

				// Prva strana
				if (currentPage > visiblePages + 1) {
				%>
				<li class="page-item"><a class="page-link"
					href="clients?action=bicycle&page=1">1</a></li>
				<li class="page-item disabled"><a class="page-link">...</a></li>
				<%
				}

				// Vidljive stranice oko trenutne
				int startPage = Math.max(1, currentPage - visiblePages);
				int endPage = Math.min(totalPages, currentPage + visiblePages);

				for (int i = startPage; i <= endPage; i++) {
				%>
				<li class="page-item <%=(i == currentPage) ? "active" : ""%>">
					<a class="page-link" href="clients?action=bicycle&page=<%=i%>"><%=i%></a>
				</li>
				<%
				}

				// Poslednja strana
				if (currentPage < totalPages - visiblePages) {
				%>
				<li class="page-item disabled"><a class="page-link">...</a></li>
				<li class="page-item"><a class="page-link"
					href="clients?action=bicycle&page=<%=totalPages%>"><%=totalPages%></a>
				</li>
				<%
				}
				%>
			</ul>
		</nav>

	</div>
	<!-- Modal za unos razloga -->
	<div class="modal fade" id="brokenModal" tabindex="-1"
		aria-labelledby="brokenModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="brokenModalLabel">Report Broken
						Bicycle</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<form id="brokenForm" method="post" action="clients">
						<input type="hidden" name="action" value="brokenVehicle">
						<input type="hidden" id="bicycleId" name="id">
						<div class="mb-3">
							<label for="reason" class="form-label">Reason</label>
							<textarea class="form-control" id="reason" name="reason" rows="3"
								maxlength="255"
								placeholder="Enter reason for the broken bicycle..."></textarea>
						</div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary"
						onclick="submitBrokenForm()">Submit</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="rentModal" tabindex="-1"
		aria-labelledby="rentModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="rentForm" method="post" action="clients">
					<div class="modal-header">
						<h5 class="modal-title" id="rentModalLabel">Rent Bicycle</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body">
						<input type="hidden" name="action" value="rentBicycle" /> <input
							type="hidden" id="rentBicycleId" name="bicycleId" />

						<div class="mb-3">
							<label for="rentalStart" class="form-label">Rental Start</label>
							<input type="datetime-local" class="form-control"
								id="rentalStart" name="rentalStart" required />
						</div>
						<div class="mb-3">
							<label for="rentalEnd" class="form-label">Rental End</label> <input
								type="datetime-local" class="form-control" id="rentalEnd"
								name="rentalEnd" required />
						</div>
						<div class="mb-3">
							<label for="startLatitude" class="form-label">Start
								Latitude</label> <input type="number" step="any" class="form-control"
								id="startLatitude" name="startLatitude" required />
						</div>
						<div class="mb-3">
							<label for="startLongitude" class="form-label">Start
								Longitude</label> <input type="number" step="any" class="form-control"
								id="startLongitude" name="startLongitude" required />
						</div>
						<div class="mb-3">
							<label for="endLatitude" class="form-label">End Latitude</label>
							<input type="number" step="any" class="form-control"
								id="endLatitude" name="endLatitude" required />
						</div>
						<div class="mb-3">
							<label for="endLongitude" class="form-label">End
								Longitude</label> <input type="number" step="any" class="form-control"
								id="endLongitude" name="endLongitude" required />
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Close</button>
						<button type="submit" class="btn btn-primary">Rent</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script>
		function openBrokenModal(bicycleId) {
			// Postavljanje ID-a bicikla u modal
			document.getElementById("bicycleId").value = bicycleId;

			// Prikazivanje modala
			const modalElement = document.getElementById('brokenModal');
			const modal = new bootstrap.Modal(modalElement);
			modal.show();
		}

		function submitBrokenForm() {
			const bicycleId = document.getElementById("bicycleId").value;
			const reason = document.getElementById("reason").value.trim();

			if (!reason) {
				alert("Please enter a reason!");
				return;
			}

			if (!bicycleId) {
				alert("Bicycle ID is missing!");
				return;
			}

			document.getElementById("brokenForm").submit();
		}
		function openRentModal(bicycleId) {
			document.getElementById("rentBicycleId").value = bicycleId;
			console.log("Bicycle ID:", document.getElementById("rentBicycleId").value);
			// Prikazivanje modala
			const modalElement = document.getElementById("rentModal");
			const modal = new bootstrap.Modal(modalElement);
			modal.show();
		}

		function submitRentForm() {
			const rentalStart = document.getElementById("rentalStart").value;
			const rentalEnd = document.getElementById("rentalEnd").value;
			const startLatitude = document.getElementById("startLatitude").value;
			const startLongitude = document.getElementById("startLongitude").value;
			const endLatitude = document.getElementById("endLatitude").value;
			const endLongitude = document.getElementById("endLongitude").value;

			// Validacija unosa
			if (!rentalStart || !rentalEnd || !startLatitude || !startLongitude
					|| !endLatitude || !endLongitude) {
				alert("All fields are required!");
				return;
			}

			// Submitovanje forme
			document.getElementById("rentForm").submit();
		}
	</script>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
