<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.unibl.etf.emobility_hub_user.invoice.InvoicePdf" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.entity.RentalEntity" %>
<%@ page import="org.unibl.etf.emobility_hub_user.util.Html" %>
<%
    RentalEntity rental = (RentalEntity) request.getAttribute("rental");
    DateTimeFormatter time = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>Ride summary - eMobilityHub</title>
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main class="app-main">
    <h1 class="page-title">Thanks for riding!</h1>
    <%@ include file="fragments/messages.jspf" %>
    <div class="card step-card">
        <div class="card-body">
            <div class="ride-amount mb-3" id="summaryTotal"><%= Html.money(rental.getPrice()) %></div>
            <dl class="detail-list row mb-0">
                <dt class="col-5">Vehicle</dt>
                <dd class="col-7"><%= Html.escape(rental.getVehicleModel()) %> (<%= Html.escape(rental.getVehicleUniqueIdentifier()) %>)</dd>
                <dt class="col-5">Start</dt>
                <dd class="col-7"><%= rental.getRentalStart().format(time) %></dd>
                <dt class="col-5">End</dt>
                <dd class="col-7"><%= rental.getRentalEnd().format(time) %></dd>
                <dt class="col-5">Duration</dt>
                <dd class="col-7"><%= InvoicePdf.formatDuration(rental.getDuration()) %></dd>
                <dt class="col-5">Distance</dt>
                <dd class="col-7"><%= Html.number(rental.getDistance(), "km") %></dd>
                <dt class="col-5">Paid with</dt>
                <dd class="col-7">card **** <%= Html.escape(rental.getPaymentCardLast4()) %></dd>
            </dl>
        </div>
    </div>
    <div class="d-grid gap-2 d-sm-flex">
        <a class="btn btn-primary btn-lg" id="downloadInvoice" href="clients?action=invoice&amp;rentalId=<%= rental.getId() %>">
            Download invoice (PDF)
        </a>
        <a class="btn btn-outline-secondary btn-lg" href="clients?action=welcome">Back to start</a>
    </div>
</main>
<%@ include file="fragments/scripts.jspf" %>
</body>
</html>
