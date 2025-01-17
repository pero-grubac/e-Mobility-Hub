<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.models.beans.UserBean" scope="session" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Promotions</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <h1>Welcome to Promotions!</h1>
        <p>Logged in as: <%= userBean.getUsername() %></p>
    </div>
    <script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
</body>
</html>
