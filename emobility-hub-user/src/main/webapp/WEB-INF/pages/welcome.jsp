<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.unibl.etf.emobility_hub_user.beans.ClientBean" %>
<%@ page import="org.unibl.etf.emobility_hub_user.util.Html" %>
<% ClientBean clientBean = (ClientBean) session.getAttribute("clientBean"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
    <title>Welcome - eMobilityHub</title>
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main class="app-main">
    <h1 class="page-title">Hi, <%= Html.escape(clientBean.getEntity().getFirstName()) %>! What would you like to do?</h1>
    <%@ include file="fragments/messages.jspf" %>
    <div class="option-grid">
        <a class="option-card" id="optionScooter" href="clients?action=scooter">
            <span class="option-icon" aria-hidden="true">&#x1F6F4;</span>Rent a scooter
        </a>
        <a class="option-card" id="optionBicycle" href="clients?action=bicycle">
            <span class="option-icon" aria-hidden="true">&#x1F6B2;</span>Rent a bicycle
        </a>
        <a class="option-card" id="optionCar" href="clients?action=car">
            <span class="option-icon" aria-hidden="true">&#x1F697;</span>Rent a car
        </a>
        <a class="option-card" id="optionProfile" href="clients?action=profile">
            <span class="option-icon" aria-hidden="true">&#x1F464;</span>My profile
        </a>
    </div>
</main>
<%@ include file="fragments/scripts.jspf" %>
</body>
</html>
