<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.AuthService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.beans.UserBean" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.Html" %>
<jsp:useBean id="authService" class="org.unibl.etf.emobility_hub_promotions.services.AuthService" scope="application" />
<%
String pageTitle = "Login";
UserBean current = (UserBean) session.getAttribute("userBean");
if (current != null && current.isManager()) {
    response.sendRedirect("home.jsp");
    return;
}

String loginMessage = "";
String infoMessage = "1".equals(request.getParameter("expired")) ? "Your session has expired. Please sign in again." : "";
String username = request.getParameter("username");
if ("POST".equals(request.getMethod()) && username != null) {
    AuthService.LoginResult result = authService.login(username, request.getParameter("password"));
    if (result == null) {
        loginMessage = "Invalid username or password.";
    } else if (!UserBean.MANAGER_ROLE.equals(result.getRole())) {
        // Valid account, but this app is for managers only: no session is created.
        loginMessage = "Only managers can use the promotions application.";
    } else {
        session.invalidate();
        UserBean user = new UserBean();
        user.signIn(result.getUsername(), result.getRole(), result.getToken());
        request.getSession(true).setAttribute("userBean", user);
        response.sendRedirect("home.jsp");
        return;
    }
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-sm-10 col-md-6 col-lg-4">
            <div class="card mt-5 shadow">
                <div class="card-body p-4">
                    <h1 class="h3 text-center mb-1">eMobilityHub</h1>
                    <p class="text-center text-muted mb-4">Promotions &amp; announcements</p>
                    <% if (!loginMessage.isEmpty()) { %>
                    <div class="alert alert-danger" role="alert" id="login-error"><%= loginMessage %></div>
                    <% } else if (!infoMessage.isEmpty()) { %>
                    <div class="alert alert-info" role="alert"><%= infoMessage %></div>
                    <% } %>
                    <form action="login.jsp" method="post" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="username" class="form-label">Username</label>
                            <input type="text" class="form-control" id="username" name="username" required
                                   value="<%= Html.escape(username) %>" autocomplete="username">
                            <div class="invalid-feedback">Enter your username.</div>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required
                                   autocomplete="current-password">
                            <div class="invalid-feedback">Enter your password.</div>
                        </div>
                        <button type="submit" id="login-button" class="btn btn-primary w-100">Login</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<%= request.getContextPath() %>/js/forms.js"></script>
</body>
</html>
