<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.unibl.etf.emobility_hub_user.beans.ClientBean" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.dto.PageMetadata" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.entity.ClientEntity" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.entity.RentalEntity" %>
<%@ page import="org.unibl.etf.emobility_hub_user.util.Html" %>
<%@ page import="org.unibl.etf.emobility_hub_user.validation.Validators" %>
<%
    ClientEntity client = ((ClientBean) session.getAttribute("clientBean")).getEntity();
    String avatar = client.getAvatarImage() == null || client.getAvatarImage().isEmpty()
            ? request.getContextPath() + "/images/default-avatar.png" : client.getAvatarImage();
    @SuppressWarnings("unchecked")
    PaginatedResponse<RentalEntity> rentals = (PaginatedResponse<RentalEntity>) request.getAttribute("rentals");
    PageMetadata pageInfo = rentals == null ? null : rentals.getPage();
    String pageLink = "clients?action=profile&page=";
    DateTimeFormatter time = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>My profile - eMobilityHub</title>
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main class="app-main">
    <h1 class="page-title">My profile</h1>
    <%@ include file="fragments/messages.jspf" %>

    <section class="card step-card">
        <div class="card-body">
            <div class="d-flex flex-column flex-sm-row gap-3 align-items-center">
                <img src="<%= Html.escape(avatar) %>" alt="Profile picture" class="avatar">
                <div class="flex-grow-1 text-center text-sm-start">
                    <div class="h5 mb-0"><%= Html.escape(client.getFirstName()) %> <%= Html.escape(client.getLastName()) %></div>
                    <div class="text-muted">@<%= Html.escape(client.getUsername()) %></div>
                    <div class="small"><%= Html.escape(client.getEmail()) %> &middot; <%= Html.escape(client.getPhoneNumber()) %></div>
                    <div class="small">ID card <%= Html.escape(client.getIdCardNumber()) %></div>
                </div>
            </div>
            <form action="clients" method="post" enctype="multipart/form-data" class="mt-3">
                <input type="hidden" name="action" value="updateAvatar">
                <label for="avatar" class="form-label small">Change picture</label>
                <div class="input-group">
                    <input type="file" class="form-control" id="avatar" name="avatar" required
                           accept="image/png,image/jpeg,image/gif,image/webp">
                    <button type="submit" class="btn btn-outline-primary">Upload</button>
                </div>
            </form>
        </div>
    </section>

    <section class="card step-card" id="rentals">
        <div class="card-body">
            <h2 class="h5">My rentals</h2>
            <% if (rentals == null || rentals.getContent() == null || rentals.getContent().isEmpty()) { %>
            <p class="text-muted mb-0">You have not rented anything yet.</p>
            <% } else { %>
            <div class="list-group list-group-flush">
                <% for (RentalEntity r : rentals.getContent()) { %>
                <div class="list-group-item px-0 d-flex justify-content-between align-items-center gap-2 flex-wrap">
                    <div>
                        <div class="fw-semibold"><%= Html.escape(r.getVehicleModel()) %>
                            <span class="text-muted small">&middot; <%= r.getVehicleType().getLabel() %></span></div>
                        <div class="small text-muted">
                            <%= r.getRentalStart().format(time) %>
                            <% if (!r.isActive()) { %> &ndash; <%= r.getRentalEnd().format(time) %> &middot; <%= Html.number(r.getDistance(), "km") %><% } %>
                        </div>
                    </div>
                    <% if (r.isActive()) { %>
                    <a class="btn btn-warning btn-sm" href="clients?action=ride">In progress &rarr;</a>
                    <% } else { %>
                    <div class="text-end">
                        <div class="fw-semibold"><%= Html.money(r.getPrice()) %></div>
                        <a class="small" href="clients?action=invoice&amp;rentalId=<%= r.getId() %>">Invoice (PDF)</a>
                    </div>
                    <% } %>
                </div>
                <% } %>
            </div>
            <div class="mt-3"><%@ include file="fragments/pagination.jspf" %></div>
            <% } %>
        </div>
    </section>

    <section class="card step-card">
        <div class="card-body">
            <h2 class="h5">Change password</h2>
            <form action="clients" method="post" class="needs-validation" novalidate id="passwordForm">
                <input type="hidden" name="action" value="changePassword">
                <div class="mb-2">
                    <label for="currentPassword" class="form-label">Current password</label>
                    <input type="password" class="form-control" id="currentPassword" name="currentPassword" required
                           autocomplete="current-password">
                    <div class="invalid-feedback">Enter your current password.</div>
                </div>
                <div class="mb-2">
                    <label for="newPassword" class="form-label">New password</label>
                    <input type="password" class="form-control" id="newPassword" name="newPassword" required
                           minlength="<%= Validators.PASSWORD_MIN_LENGTH %>" maxlength="72" autocomplete="new-password">
                    <div class="invalid-feedback">At least <%= Validators.PASSWORD_MIN_LENGTH %> characters.</div>
                </div>
                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">Repeat new password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required
                           data-match="#newPassword" autocomplete="new-password">
                    <div class="invalid-feedback">The passwords must match.</div>
                </div>
                <button type="submit" class="btn btn-primary btn-block-sm">Change password</button>
            </form>
        </div>
    </section>

    <section class="card step-card border-danger-subtle">
        <div class="card-body">
            <h2 class="h5 text-danger">Deactivate account</h2>
            <p class="small text-muted">You will be signed out and will not be able to sign in again.</p>
            <form action="clients" method="post"
                  onsubmit="return confirm('Are you sure you want to deactivate your account?');">
                <input type="hidden" name="action" value="deactivate">
                <button type="submit" class="btn btn-outline-danger btn-block-sm">Deactivate my account</button>
            </form>
        </div>
    </section>
</main>
<%@ include file="fragments/scripts.jspf" %>
</body>
</html>
