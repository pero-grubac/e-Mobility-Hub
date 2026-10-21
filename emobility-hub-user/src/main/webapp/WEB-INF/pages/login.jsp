<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>Sign in - eMobilityHub</title>
</head>
<body>
<main class="app-main">
    <div class="card auth-card shadow-sm">
        <div class="card-body p-4">
            <div class="auth-brand mb-1">eMobilityHub</div>
            <p class="text-center text-muted mb-4">Rent an electric scooter, bicycle or car</p>
            <%@ include file="fragments/messages.jspf" %>
            <form action="clients" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="action" value="login">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" autocomplete="username"
                           required autofocus>
                    <div class="invalid-feedback">Enter your username.</div>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password"
                           autocomplete="current-password" required>
                    <div class="invalid-feedback">Enter your password.</div>
                </div>
                <button type="submit" id="loginButton" class="btn btn-primary w-100">Sign in</button>
            </form>
            <p class="mt-3 mb-0 text-center">No account yet? <a href="clients?action=register">Register</a></p>
        </div>
    </div>
</main>
<%@ include file="fragments/scripts.jspf" %>
</body>
</html>
