<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="org.unibl.etf.emobility_hub_user.models.entity.ClientEntity"%>
<%@ page import="java.util.Objects"%>
<jsp:useBean id="clientBean"
	type="org.unibl.etf.emobility_hub_user.beans.ClientBean"
	scope="session" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Profile</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/bootstrap.min.css">
</head>
<body class="bg-light">

	<jsp:include page="header.html" />

	<div class="container mt-5">
		<h2 class="mb-4">My Profile</h2>

		<%
		String avatarPath = clientBean.getEntity().getAvatarImage();
		if (Objects.isNull(avatarPath) || avatarPath.isEmpty()) {
			avatarPath = request.getContextPath() + "/images/default-avatar.png";
		}
		%>

		<div class="row">
			<div class="col-md-4 text-center">
				<img src="<%=avatarPath%>" alt="Profile Image"
					class="img-thumbnail mb-3" style="width: 100%; max-width: 300px;">

				<form action="clients" method="post" enctype="multipart/form-data"
					class="mt-3">
					<input type="hidden" name="action" value="updateAvatar"> <input
						type="file" id="avatar" name="avatar" style="display: none;"
						onchange="form.submit();">
					<button type="button" class="btn btn-primary w-100"
						onclick="document.getElementById('avatar').click();">
						Upload & Update Picture</button>
				</form>

			</div>

			<div class="col-md-8">
				<form action="clients" method="post">
					<input type="hidden" name="action" value="changePassword">

					<div class="mb-3">
						<label for="username" class="form-label">Username</label> <input
							type="text" id="username" name="username" class="form-control"
							value="<%=clientBean.getEntity().getUsername()%>" readonly>
					</div>

					<div class="mb-3">
						<label for="firstName" class="form-label">First Name</label> <input
							type="text" id="firstName" name="firstName" class="form-control"
							value="<%=clientBean.getEntity().getFirstName()%>" readonly>
					</div>

					<div class="mb-3">
						<label for="lastName" class="form-label">Last Name</label> <input
							type="text" id="lastName" name="lastName" class="form-control"
							value="<%=clientBean.getEntity().getLastName()%>" readonly>
					</div>

					<div class="mb-3">
						<label for="email" class="form-label">Email</label> <input
							type="email" id="email" name="email" class="form-control"
							value="<%=clientBean.getEntity().getEmail()%>" readonly>
					</div>

					<div class="mb-3">
						<label for="phoneNumber" class="form-label">Phone Number</label> <input
							type="text" id="phoneNumber" name="phoneNumber"
							class="form-control"
							value="<%=clientBean.getEntity().getPhoneNumber()%>" readonly>
					</div>

					<div class="mb-3">
						<label for="idCardNumber" class="form-label">ID Card
							Number</label> <input type="text" id="idCardNumber" name="idCardNumber"
							class="form-control"
							value="<%=clientBean.getEntity().getIdCardNumber()%>" readonly>
					</div>

					<div class="mb-3">
						<label for="password" class="form-label">New Password</label> <input
							type="password" id="password" name="password"
							class="form-control" placeholder="Enter new password" required>
					</div>

					<div class="d-flex justify-content-between mb-3">
						<button type="submit" class="btn btn-primary">Update
							Password</button>
						<button type="button" class="btn btn-success"
							onclick="redirectToRentals()">Rentals</button>
						<button type="button" class="btn btn-danger"
							onclick="deactivateAccount()">Deactivate Account</button>
					</div>

				</form>
			</div>
		</div>
	</div>

	<script>
		function deactivateAccount() {
			if (confirm("Are you sure you want to deactivate your account?")) {
				window.location.href = "clients?action=deactivate";
			}
		}
		function redirectToRentals() {
			window.location.href = "clients?action=rentals";
		}
	</script>

	<script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
</body>
</html>
