<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.dto.PageMetadata" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.entity.ElectricCarEntity" %>
<%@ page import="org.unibl.etf.emobility_hub_user.util.Html" %>
<%
    @SuppressWarnings("unchecked")
    PaginatedResponse<ElectricCarEntity> vehicles = (PaginatedResponse<ElectricCarEntity>) request.getAttribute("vehicles");
    PageMetadata pageInfo = vehicles == null ? null : vehicles.getPage();
    String pageLink = "clients?action=car&page=";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>Electric cars - eMobilityHub</title>
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main class="app-main">
    <h1 class="page-title">Electric cars</h1>
    <%@ include file="fragments/messages.jspf" %>
    <% if (vehicles == null || vehicles.getContent() == null || vehicles.getContent().isEmpty()) { %>
    <div class="alert alert-info">No cars are free right now. Please check again later.</div>
    <% } else { %>
    <div class="row g-3">
        <% for (ElectricCarEntity v : vehicles.getContent()) { %>
        <div class="col-12 col-sm-6 col-lg-4">
            <div class="card vehicle-card h-100">
                <img src="<%= Html.escape(Html.vehicleImage(v.getImage(), request.getContextPath())) %>" class="card-img-top"
                     alt="<%= Html.escape(v.getModel()) %>">
                <div class="card-body d-flex flex-column">
                    <h2 class="h5 card-title mb-1"><%= Html.escape(v.getModel()) %></h2>
                    <div class="text-muted small mb-2"><%= Html.escape(v.getManufacturerName()) %> &middot; <%= Html.escape(v.getUniqueIdentifier()) %></div>
                    <p class="mb-2"><%= Html.escape(v.getDescription()) %><br><span class="small text-muted">In service since <%= v.getPurchaseDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/yyyy")) %></span></p>
                    <div class="price-tag mb-3"><%= String.format(Locale.US, "%.2f", v.getRentPrice()) %> EUR / hour</div>
                    <div class="d-flex gap-2 mt-auto">
                        <a class="btn btn-primary flex-grow-1" data-rent="<%= v.getId() %>"
                           href="clients?action=rent&amp;vehicleId=<%= v.getId() %>">Rent</a>
                        <button type="button" class="btn btn-outline-warning" data-report="<%= v.getId() %>"
                                data-name="<%= Html.escape(v.getModel()) %>">Report broken</button>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>
    <div class="mt-3"><%@ include file="fragments/pagination.jspf" %></div>
    <% } %>
</main>
<%@ include file="fragments/brokenModal.jspf" %>
<%@ include file="fragments/scripts.jspf" %>
</body>
</html>
