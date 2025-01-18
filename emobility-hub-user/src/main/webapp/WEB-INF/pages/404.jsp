<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>Page Not Found</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>
<body class="bg-light">
    <div class="container text-center mt-5">
        <h1 class="display-1 text-danger">404</h1>
        <p class="lead">Oops! The page you are looking for cannot be found.</p>
        <a href="clients?action=welcome" class="btn btn-primary">
            <i class="bi bi-house"></i> Back to Home
        </a>
    </div>

    <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
</body>
</html>
