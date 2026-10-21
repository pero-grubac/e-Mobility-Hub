<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.entity.RentalEntity" %>
<%@ page import="org.unibl.etf.emobility_hub_user.util.Html" %>
<%
    RentalEntity ride = (RentalEntity) request.getAttribute("ride");
    long startMillis = ride.getRentalStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>Ride in progress - eMobilityHub</title>
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main class="app-main">
    <h1 class="page-title">Ride in progress</h1>
    <%@ include file="fragments/messages.jspf" %>

    <div class="card step-card">
        <div class="card-body">
            <div class="d-flex gap-3 align-items-center mb-3">
                <img src="<%= Html.escape(Html.vehicleImage(ride.getVehicleImage(), request.getContextPath())) %>" alt=""
                     width="96" height="64" class="rounded object-fit-cover">
                <div>
                    <div class="fw-semibold"><%= Html.escape(ride.getVehicleModel()) %></div>
                    <div class="text-muted small"><%= ride.getVehicleType().getLabel() %> &middot; <%= Html.escape(ride.getVehicleUniqueIdentifier()) %></div>
                    <div class="small">Started <%= ride.getRentalStart().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) %></div>
                </div>
            </div>
            <div class="text-muted text-center small">Ride time</div>
            <div class="ride-clock" id="rideClock">00:00:00</div>
            <div class="text-muted text-center small mt-2">Amount so far</div>
            <div class="ride-amount" id="rideAmount">0.00 EUR</div>
            <div class="text-center small text-muted">
                <%= String.format(Locale.US, "%.2f", ride.getRentPrice()) %> EUR / hour &middot; card **** <%= Html.escape(ride.getPaymentCardLast4()) %>
            </div>
        </div>
    </div>

    <form id="finishForm" method="post" action="clients" class="card step-card needs-validation" novalidate>
        <div class="card-body">
            <h2 class="h6">Finish the ride</h2>
            <input type="hidden" name="action" value="finishRide">
            <input type="hidden" name="rentalId" value="<%= ride.getId() %>">
            <div class="row g-2">
                <div class="col-6">
                    <label for="endLatitude" class="form-label">Drop-off latitude</label>
                    <input type="number" step="any" min="-90" max="90" class="form-control" id="endLatitude"
                           name="endLatitude" required>
                    <div class="invalid-feedback">-90 to 90</div>
                </div>
                <div class="col-6">
                    <label for="endLongitude" class="form-label">Drop-off longitude</label>
                    <input type="number" step="any" min="-180" max="180" class="form-control" id="endLongitude"
                           name="endLongitude" required>
                    <div class="invalid-feedback">-180 to 180</div>
                </div>
            </div>
            <button type="button" id="useEndLocation" class="btn btn-outline-primary btn-sm mt-2" data-locate
                    data-lat="#endLatitude" data-lng="#endLongitude" data-status="#endLocationStatus">
                Use my location
            </button>
            <div id="endLocationStatus" class="form-text" aria-live="polite">Where are you leaving the vehicle?</div>
            <button type="submit" id="finishRide" class="btn btn-danger btn-lg w-100 mt-3">Finish ride and pay</button>
        </div>
    </form>
</main>
<%@ include file="fragments/scripts.jspf" %>
<script>
    emh.startRideClock(<%= startMillis %>, <%= System.currentTimeMillis() %>, <%= ride.getRentPrice() %>,
        document.getElementById('rideClock'), document.getElementById('rideAmount'));
</script>
</body>
</html>
