<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.unibl.etf.emobility_hub_user.beans.ClientBean" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.entity.TransportVehicleEntity" %>
<%@ page import="org.unibl.etf.emobility_hub_user.models.entity.VehicleType" %>
<%@ page import="org.unibl.etf.emobility_hub_user.util.Html" %>
<%@ page import="org.unibl.etf.emobility_hub_user.validation.Validators" %>
<%
    TransportVehicleEntity vehicle = (TransportVehicleEntity) request.getAttribute("vehicle");
    ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");
    boolean isCar = vehicle.getType() == VehicleType.CAR;
    String rate = String.format(Locale.US, "%.2f", vehicle.getRentPrice());
    int step = 1;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>Rent <%= Html.escape(vehicle.getModel()) %> - eMobilityHub</title>
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main class="app-main">
    <a href="clients?action=<%= vehicle.getType().getAction() %>" class="small">&larr; Back to the list</a>
    <h1 class="page-title mt-2">Rent <%= Html.escape(vehicle.getModel()) %></h1>
    <%@ include file="fragments/messages.jspf" %>

    <div class="card step-card">
        <div class="card-body d-flex gap-3 align-items-center">
            <img src="<%= Html.escape(Html.vehicleImage(vehicle.getImage(), request.getContextPath())) %>" alt=""
                 width="96" height="64" class="rounded object-fit-cover">
            <div>
                <div class="fw-semibold"><%= vehicle.getType().getLabel() %> &middot; <%= Html.escape(vehicle.getManufacturerName()) %></div>
                <div class="text-muted small"><%= Html.escape(vehicle.getUniqueIdentifier()) %></div>
                <div class="price-tag"><%= rate %> EUR / hour</div>
            </div>
        </div>
    </div>

    <form id="rentForm" method="post" action="clients" class="needs-validation" novalidate>
        <input type="hidden" name="action" value="startRide">
        <input type="hidden" name="vehicleId" value="<%= vehicle.getId() %>">

        <section class="card step-card">
            <div class="card-body">
                <h2 class="h6"><span class="step-number"><%= step++ %></span>Where are you picking it up?</h2>
                <div class="row g-2">
                    <div class="col-6">
                        <label for="startLatitude" class="form-label">Latitude</label>
                        <input type="number" step="any" min="-90" max="90" class="form-control" id="startLatitude"
                               name="startLatitude" required
                               value="<%= vehicle.getLatitude() == null ? "" : vehicle.getLatitude() %>">
                        <div class="invalid-feedback">-90 to 90</div>
                    </div>
                    <div class="col-6">
                        <label for="startLongitude" class="form-label">Longitude</label>
                        <input type="number" step="any" min="-180" max="180" class="form-control" id="startLongitude"
                               name="startLongitude" required
                               value="<%= vehicle.getLongitude() == null ? "" : vehicle.getLongitude() %>">
                        <div class="invalid-feedback">-180 to 180</div>
                    </div>
                </div>
                <button type="button" id="useStartLocation" class="btn btn-outline-primary btn-sm mt-2" data-locate
                        data-lat="#startLatitude" data-lng="#startLongitude" data-status="#startLocationStatus">
                    Use my location
                </button>
                <div id="startLocationStatus" class="form-text" aria-live="polite">
                    Filled in with where the vehicle is parked. Type your position or detect it automatically.
                </div>
            </div>
        </section>

        <% if (isCar) { %>
        <section class="card step-card">
            <div class="card-body">
                <h2 class="h6"><span class="step-number"><%= step++ %></span>Identity document and driver's licence</h2>
                <div class="mb-2">
                    <label for="documentType" class="form-label">Document</label>
                    <select class="form-select" id="documentType" name="documentType" required>
                        <option value="ID_CARD" selected>ID card (local citizens)</option>
                        <option value="PASSPORT">Passport (foreign citizens)</option>
                    </select>
                </div>
                <div class="mb-2">
                    <label for="documentNumber" class="form-label">Document number</label>
                    <input type="text" class="form-control" id="documentNumber" name="documentNumber" required
                           pattern="<%= Validators.DOCUMENT_PATTERN %>"
                           value="<%= Html.escape(clientBean.getEntity().getIdCardNumber()) %>">
                    <div class="invalid-feedback">5-30 letters, digits or dashes.</div>
                </div>
                <div>
                    <label for="driverLicenseNumber" class="form-label">Driver's licence number</label>
                    <input type="text" class="form-control" id="driverLicenseNumber" name="driverLicenseNumber" required
                           pattern="<%= Validators.DOCUMENT_PATTERN %>">
                    <div class="invalid-feedback">5-30 letters, digits or dashes.</div>
                </div>
            </div>
        </section>
        <% } %>

        <section class="card step-card">
            <div class="card-body">
                <h2 class="h6"><span class="step-number"><%= step++ %></span>Payment card</h2>
                <div class="mb-2">
                    <label for="cardHolder" class="form-label">Name on card</label>
                    <input type="text" class="form-control" id="cardHolder" name="cardHolder" required maxlength="100"
                           autocomplete="cc-name"
                           value="<%= Html.escape(clientBean.getEntity().getFirstName() + " " + clientBean.getEntity().getLastName()) %>">
                    <div class="invalid-feedback">Enter the name on the card.</div>
                </div>
                <div class="mb-2">
                    <label for="cardNumber" class="form-label">Card number</label>
                    <input type="text" class="form-control" id="cardNumber" name="cardNumber" required inputmode="numeric"
                           autocomplete="cc-number" placeholder="4242 4242 4242 4242" maxlength="23">
                    <div class="invalid-feedback">This card number is not valid.</div>
                </div>
                <div class="row g-2">
                    <div class="col-6">
                        <label for="cardExpiry" class="form-label">Expires (MM/YY)</label>
                        <input type="text" class="form-control" id="cardExpiry" name="cardExpiry" required
                               inputmode="numeric" autocomplete="cc-exp" placeholder="MM/YY" maxlength="5">
                        <div class="invalid-feedback">Not a valid future date.</div>
                    </div>
                    <div class="col-6">
                        <label for="cardCvv" class="form-label">CVV</label>
                        <input type="password" class="form-control" id="cardCvv" name="cardCvv" required
                               inputmode="numeric" autocomplete="cc-csc" pattern="[0-9]{3,4}" maxlength="4">
                        <div class="invalid-feedback">3 or 4 digits.</div>
                    </div>
                </div>
                <p class="form-text mb-0">The ride costs <%= rate %> EUR per hour (at least one minute) and is charged when you finish it.</p>
            </div>
        </section>

        <button type="button" id="reviewPayment" class="btn btn-primary btn-lg w-100">Continue to payment</button>

        <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2 class="modal-title h5" id="confirmModalLabel">Confirm payment</h2>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <dl class="detail-list mb-0">
                            <dt>Vehicle</dt><dd><%= Html.escape(vehicle.getModel()) %> (<%= Html.escape(vehicle.getUniqueIdentifier()) %>)</dd>
                            <dt>Rate</dt><dd><%= rate %> EUR / hour</dd>
                            <dt>Card</dt><dd id="confirmCard"></dd>
                        </dl>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Back</button>
                        <button type="submit" id="confirmPayment" class="btn btn-success">Confirm payment and start</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</main>
<%@ include file="fragments/scripts.jspf" %>
<script>
    (function () {
        var form = document.getElementById('rentForm');
        var cardNumber = document.getElementById('cardNumber');
        var cardExpiry = document.getElementById('cardExpiry');

        var checkCard = function () {
            cardNumber.setCustomValidity(emh.luhn(cardNumber.value) ? '' : 'invalid');
            cardExpiry.setCustomValidity(emh.expiryValid(cardExpiry.value) ? '' : 'invalid');
        };
        cardNumber.addEventListener('input', checkCard);
        cardExpiry.addEventListener('input', checkCard);

        document.getElementById('reviewPayment').addEventListener('click', function () {
            checkCard();
            form.classList.add('was-validated');
            if (!form.checkValidity()) {
                form.querySelector(':invalid').focus();
                return;
            }
            var digits = cardNumber.value.replace(/[\s-]/g, '');
            document.getElementById('confirmCard').textContent = '**** **** **** ' + digits.slice(-4);
            bootstrap.Modal.getOrCreateInstance(document.getElementById('confirmModal')).show();
        });
    })();
</script>
</body>
</html>
