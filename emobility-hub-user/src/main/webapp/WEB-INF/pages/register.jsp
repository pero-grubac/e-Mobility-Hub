<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.unibl.etf.emobility_hub_user.util.Html" %>
<%@ page import="org.unibl.etf.emobility_hub_user.validation.Validators" %>
<%
    @SuppressWarnings("unchecked")
    List<String> errors = (List<String>) request.getAttribute("errors");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>Register - eMobilityHub</title>
</head>
<body>
<main class="app-main">
    <div class="card auth-card shadow-sm">
        <div class="card-body p-4">
            <h1 class="h4 text-center mb-3">Create an account</h1>
            <% if (errors != null && !errors.isEmpty()) { %>
            <div class="alert alert-danger" role="alert">
                <ul class="mb-0 ps-3">
                    <% for (String error : errors) { %><li><%= Html.escape(error) %></li><% } %>
                </ul>
            </div>
            <% } %>
            <form action="clients" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                <input type="hidden" name="action" value="register">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required
                           pattern="<%= Validators.USERNAME_PATTERN %>" autocomplete="username"
                           value="<%= Html.escape(request.getParameter("username")) %>">
                    <div class="invalid-feedback">3-30 letters, digits, dots or underscores.</div>
                </div>
                <div class="row g-2">
                    <div class="col-sm-6 mb-3">
                        <label for="firstName" class="form-label">First name</label>
                        <input type="text" class="form-control" id="firstName" name="firstName" required maxlength="255"
                               value="<%= Html.escape(request.getParameter("firstName")) %>">
                        <div class="invalid-feedback">Enter your first name.</div>
                    </div>
                    <div class="col-sm-6 mb-3">
                        <label for="lastName" class="form-label">Last name</label>
                        <input type="text" class="form-control" id="lastName" name="lastName" required maxlength="255"
                               value="<%= Html.escape(request.getParameter("lastName")) %>">
                        <div class="invalid-feedback">Enter your last name.</div>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required maxlength="255"
                           autocomplete="email" value="<%= Html.escape(request.getParameter("email")) %>">
                    <div class="invalid-feedback">Enter a valid email address.</div>
                </div>
                <div class="mb-3">
                    <label for="phoneNumber" class="form-label">Phone number</label>
                    <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" required
                           pattern="<%= Validators.PHONE_PATTERN %>" placeholder="+387-65-123-456" autocomplete="tel"
                           value="<%= Html.escape(request.getParameter("phoneNumber")) %>">
                    <div class="invalid-feedback">Enter a valid phone number, e.g. +387-65-123-456.</div>
                </div>
                <div class="mb-3">
                    <label for="idCardNumber" class="form-label">ID card number</label>
                    <input type="text" class="form-control" id="idCardNumber" name="idCardNumber" required
                           pattern="<%= Validators.ID_CARD_PATTERN %>"
                           value="<%= Html.escape(request.getParameter("idCardNumber")) %>">
                    <div class="invalid-feedback">5-20 letters, digits or dashes.</div>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required
                           minlength="<%= Validators.PASSWORD_MIN_LENGTH %>" maxlength="72" autocomplete="new-password">
                    <div class="invalid-feedback">At least <%= Validators.PASSWORD_MIN_LENGTH %> characters.</div>
                </div>
                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">Repeat password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required
                           data-match="#password" autocomplete="new-password">
                    <div class="invalid-feedback">The passwords must match.</div>
                </div>
                <div class="mb-3">
                    <label for="avatar" class="form-label">Profile picture <span class="text-muted">(optional)</span></label>
                    <input type="file" class="form-control" id="avatar" name="avatar" accept="image/png,image/jpeg,image/gif,image/webp">
                </div>
                <button type="submit" id="registerButton" class="btn btn-primary w-100">Create account</button>
            </form>
            <p class="mt-3 mb-0 text-center">Already registered? <a href="clients">Sign in</a></p>
        </div>
    </div>
</main>
<%@ include file="fragments/scripts.jspf" %>
</body>
</html>
