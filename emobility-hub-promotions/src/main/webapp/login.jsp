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
<body>
<div class="login-page">
    <section class="login-brand" aria-hidden="true">
        <div class="login-logo">
            <svg viewBox="0 0 48 48">
                <circle cx="24" cy="24" r="22" fill="rgba(255,255,255,.14)"/>
                <path d="M27 6 13 27h10l-3 15 15-22H25z" fill="#ffd54f"/>
            </svg>
            <span>eMobilityHub</span>
        </div>
        <div>
            <h1>Promotions portal</h1>
            <p class="lead-text">Campaigns and news for everyone who rents with us, published on the site and in the RSS feed.</p>
        </div>
        <ul class="login-chips">
            <li>Promotions</li>
            <li>Announcements</li>
            <li>RSS</li>
        </ul>
        <p class="login-note">For managers only</p>
    </section>

    <main class="login-main">
        <div class="login-card">
            <h2>Sign in</h2>
            <p class="text-muted mb-4">Use your manager account.</p>
            <% if (!loginMessage.isEmpty()) { %>
            <div class="alert alert-danger py-2" role="alert" id="login-error"><%= loginMessage %></div>
            <% } else if (!infoMessage.isEmpty()) { %>
            <div class="alert alert-info py-2" role="alert"><%= infoMessage %></div>
            <% } %>
            <form action="login.jsp" method="post" class="needs-validation" novalidate>
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required
                           value="<%= Html.escape(username) %>" autocomplete="username">
                    <div class="invalid-feedback">Enter your username.</div>
                </div>
                <div class="mb-4">
                    <label for="password" class="form-label">Password</label>
                    <div class="password-field">
                        <input type="password" class="form-control" id="password" name="password" required
                               autocomplete="current-password">
                        <button type="button" class="password-toggle" data-toggle-password="password"
                                aria-label="Show password">Show</button>
                        <div class="invalid-feedback">Enter your password.</div>
                    </div>
                </div>
                <button type="submit" id="login-button" class="btn btn-primary w-100">Sign in</button>
            </form>
        </div>
    </main>
</div>
<script src="<%= request.getContextPath() %>/js/forms.js"></script>
</body>
</html>
