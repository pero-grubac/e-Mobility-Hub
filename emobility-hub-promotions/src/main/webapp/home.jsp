<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.ApiException" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.AnnouncementService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.PromotionService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.beans.AnnouncementResponseBean" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.beans.PromotionResponseBean" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.Html" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.beans.UserBean" scope="session" />
<%!
    // A page number from the query string; anything invalid means the first page.
    private static int pageParam(String value) {
        try {
            return Math.max(0, Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
%>
<%
String pageTitle = "Overview";
final int size = 6;

String search = request.getParameter("search");
search = search == null || search.trim().isEmpty() ? null : search.trim();
int promotionPage = pageParam(request.getParameter("promotionPage"));
int announcementPage = pageParam(request.getParameter("announcementPage"));

List<PromotionResponseBean> promotions = Collections.emptyList();
List<AnnouncementResponseBean> announcements = Collections.emptyList();
int promotionPages = 0;
int announcementPages = 0;
long promotionTotal = 0;
long announcementTotal = 0;
String errorMessage = null;

try {
    PaginatedResponse<PromotionResponseBean> promotionResult =
            new PromotionService().getPromotions(userBean.getToken(), promotionPage, size, search);
    promotions = promotionResult.getContent();
    promotionPages = promotionResult.getPage().getTotalPages();
    promotionTotal = promotionResult.getPage().getTotalElements();

    PaginatedResponse<AnnouncementResponseBean> announcementResult =
            new AnnouncementService().getAnnouncements(userBean.getToken(), announcementPage, size, search);
    announcements = announcementResult.getContent();
    announcementPages = announcementResult.getPage().getTotalPages();
    announcementTotal = announcementResult.getPage().getTotalElements();
} catch (ApiException e) {
    if (e.isSessionExpired()) {
        session.invalidate();
        response.sendRedirect("login.jsp?expired=1");
        return;
    }
    errorMessage = e.getMessage();
} catch (Exception e) {
    errorMessage = "The API is not reachable: " + e.getMessage();
}

// Pagination links keep the search and the other list's page.
String searchQuery = search == null ? "" : "&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8);
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
String flash = (String) session.getAttribute("flash");
session.removeAttribute("flash");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="bg-light">
<%@ include file="/WEB-INF/jspf/navbar.jspf" %>
<main class="container py-4">
    <% if (flash != null) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <%= Html.escape(flash) %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% } %>

    <form method="get" action="home.jsp" class="mb-4" id="searchForm" role="search">
        <label for="search" class="form-label fw-semibold">Search promotions and announcements by content</label>
        <div class="input-group">
            <input type="search" class="form-control" id="search" name="search" maxlength="255"
                   placeholder="e.g. scooter, discount, holiday" value="<%= Html.escape(search) %>">
            <button class="btn btn-primary" type="submit" id="search-button">Search</button>
            <% if (search != null) { %>
            <a class="btn btn-outline-secondary" href="home.jsp">Clear</a>
            <% } %>
        </div>
    </form>

    <% if (errorMessage != null) { %>
    <div class="alert alert-danger" role="alert"><%= Html.escape(errorMessage) %></div>
    <% } %>

    <section class="mb-5" id="promotions">
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
            <h2 class="h4 mb-0">Promotions <span class="badge bg-secondary"><%= promotionTotal %></span></h2>
            <a class="btn btn-primary btn-sm" href="createPromotion.jsp" id="create-promotion">New promotion</a>
        </div>
        <div class="row g-3">
            <% if (promotions.isEmpty()) { %>
            <div class="col-12"><div class="alert alert-warning mb-0">No promotions found.</div></div>
            <% } %>
            <% for (PromotionResponseBean promotion : promotions) { %>
            <div class="col-sm-6 col-lg-4">
                <a class="card h-100 item-card text-decoration-none text-body" href="promotionDetails.jsp?id=<%= promotion.getId() %>">
                    <div class="card-body">
                        <h3 class="h6 card-title"><%= Html.escape(promotion.getTitle()) %></h3>
                        <p class="card-text card-preview"><%= Html.preview(promotion.getContent(), 90) %></p>
                    </div>
                    <div class="card-footer small text-muted">
                        <%= promotion.getStartDate().format(formatter) %> – <%= promotion.getEndDate().format(formatter) %>
                    </div>
                </a>
            </div>
            <% } %>
        </div>
        <% if (promotionPages > 1) { %>
        <nav aria-label="Promotion pages" class="mt-3">
            <ul class="pagination justify-content-center">
                <% for (int i = 0; i < promotionPages; i++) { %>
                <li class="page-item <%= i == promotionPage ? "active" : "" %>">
                    <a class="page-link" href="home.jsp?promotionPage=<%= i %>&announcementPage=<%= announcementPage %><%= searchQuery %>#promotions"><%= i + 1 %></a>
                </li>
                <% } %>
            </ul>
        </nav>
        <% } %>
    </section>

    <section id="announcements">
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
            <h2 class="h4 mb-0">Announcements <span class="badge bg-secondary"><%= announcementTotal %></span></h2>
            <a class="btn btn-primary btn-sm" href="createAnnouncement.jsp" id="create-announcement">New announcement</a>
        </div>
        <div class="row g-3">
            <% if (announcements.isEmpty()) { %>
            <div class="col-12"><div class="alert alert-warning mb-0">No announcements found.</div></div>
            <% } %>
            <% for (AnnouncementResponseBean announcement : announcements) { %>
            <div class="col-sm-6 col-lg-4">
                <a class="card h-100 item-card text-decoration-none text-body" href="announcementDetails.jsp?id=<%= announcement.getId() %>">
                    <div class="card-body">
                        <h3 class="h6 card-title"><%= Html.escape(announcement.getTitle()) %></h3>
                        <p class="card-text card-preview"><%= Html.preview(announcement.getContent(), 90) %></p>
                    </div>
                    <div class="card-footer small text-muted">
                        Updated <%= announcement.getUpdateDate().format(formatter) %>
                    </div>
                </a>
            </div>
            <% } %>
        </div>
        <% if (announcementPages > 1) { %>
        <nav aria-label="Announcement pages" class="mt-3">
            <ul class="pagination justify-content-center">
                <% for (int i = 0; i < announcementPages; i++) { %>
                <li class="page-item <%= i == announcementPage ? "active" : "" %>">
                    <a class="page-link" href="home.jsp?promotionPage=<%= promotionPage %>&announcementPage=<%= i %><%= searchQuery %>#announcements"><%= i + 1 %></a>
                </li>
                <% } %>
            </ul>
        </nav>
        <% } %>
    </section>
</main>
<script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
</body>
</html>
