<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.ApiException" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.PromotionService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.FormValidation" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.Html" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.beans.UserBean" scope="session" />
<%
String pageTitle = "New promotion";
String title = request.getParameter("title");
String content = request.getParameter("content");
String startDate = request.getParameter("startDate");
String endDate = request.getParameter("endDate");
List<String> errors = Collections.emptyList();

if ("POST".equals(request.getMethod())) {
    errors = FormValidation.validatePromotion(title, content, startDate, endDate);
    if (errors.isEmpty()) {
        try {
            PromotionRequest promotionRequest = new PromotionRequest();
            promotionRequest.setTitle(title.trim());
            promotionRequest.setContent(content.trim());
            promotionRequest.setStartDate(startDate);
            promotionRequest.setEndDate(endDate);
            new PromotionService().createPromotion(userBean.getToken(), promotionRequest);
            session.setAttribute("flash", "Promotion \"" + title.trim() + "\" was created.");
            response.sendRedirect("home.jsp");
            return;
        } catch (ApiException e) {
            if (e.isSessionExpired()) {
                session.invalidate();
                response.sendRedirect("login.jsp?expired=1");
                return;
            }
            errors = List.of(e.getMessage());
        }
    }
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="bg-light">
<%@ include file="/WEB-INF/jspf/navbar.jspf" %>
<main class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <h1 class="h3 mb-3">New promotion</h1>
            <% if (!errors.isEmpty()) { %>
            <div class="alert alert-danger" role="alert">
                <ul class="mb-0"><% for (String error : errors) { %><li><%= Html.escape(error) %></li><% } %></ul>
            </div>
            <% } %>
            <form method="post" action="createPromotion.jsp" class="card shadow-sm needs-validation" novalidate id="promotion-form">
                <div class="card-body">
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" id="title" name="title" required
                               maxlength="<%= FormValidation.TITLE_MAX %>" value="<%= Html.escape(title) %>">
                        <div class="invalid-feedback">A title is required.</div>
                    </div>
                    <div class="mb-3">
                        <label for="content" class="form-label">Description</label>
                        <textarea class="form-control" id="content" name="content" rows="4" required
                                  maxlength="<%= FormValidation.PROMOTION_CONTENT_MAX %>"><%= Html.escape(content) %></textarea>
                        <div class="invalid-feedback">A description is required (up to <%= FormValidation.PROMOTION_CONTENT_MAX %> characters).</div>
                    </div>
                    <div class="row g-3">
                        <div class="col-sm-6">
                            <label for="startDate" class="form-label">Starts</label>
                            <input type="datetime-local" class="form-control" id="startDate" name="startDate" required
                                   value="<%= Html.escape(startDate) %>">
                            <div class="invalid-feedback">Choose when the promotion starts.</div>
                        </div>
                        <div class="col-sm-6">
                            <label for="endDate" class="form-label">Ends</label>
                            <input type="datetime-local" class="form-control" id="endDate" name="endDate" required
                                   value="<%= Html.escape(endDate) %>">
                            <div class="invalid-feedback">The end must be after the start.</div>
                        </div>
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between">
                    <a class="btn btn-outline-secondary" href="home.jsp">Cancel</a>
                    <button type="submit" class="btn btn-primary" id="save-promotion">Create promotion</button>
                </div>
            </form>
        </div>
    </div>
</main>
<script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/js/forms.js"></script>
</body>
</html>
