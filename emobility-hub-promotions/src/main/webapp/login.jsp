<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.models.beans.UserBean" scope="session" />
<jsp:useBean id="authService" class="org.unibl.etf.emobility_hub_promotions.services.AuthService" scope="application" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%
    String loginMessage = "";
    if (authService == null) {
        loginMessage = "Authentication service is not available.";
    } else if (request.getParameter("username") != null && request.getParameter("password") != null) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String token = authService.login(username, password);

        if (token != null) {
            userBean.setUsername(username);
            userBean.setPassword(password);
            userBean.setLoggedIn(true);
            userBean.setToken(token);

            response.sendRedirect("promotions.jsp");
            return;
        } else {
            loginMessage = "Invalid username or password!";
        }
    }
%>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card mt-5 shadow">
                    <div class="card-body">
                        <h2 class="text-center mb-4">Login</h2>

                        <% if (!loginMessage.isEmpty()) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= loginMessage %>
                            </div>
                        <% } %>

                        <form action="login.jsp" method="post">
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Login</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
</body>
</html>
