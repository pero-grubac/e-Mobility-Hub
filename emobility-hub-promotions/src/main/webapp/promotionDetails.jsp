<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.ApiException" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.PromotionService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.beans.PromotionResponseBean" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.FormValidation" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.Html" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.beans.UserBean" scope="session" />
<%
String pageTitle = "Promotion";
PromotionService promotionService = new PromotionService();
List<String> errors = Collections.emptyList();
PromotionResponseBean promotion = null;
String title = request.getParameter("title");
String content = request.getParameter("content");
String startDate = request.getParameter("startDate");
String endDate = request.getParameter("endDate");

Long id = null;
try {
    id = Long.valueOf(request.getParameter("id"));
} catch (NumberFormatException e) {
    // Shown below as "not found".
}

try {
    if (id != null && "POST".equals(request.getMethod())) {
        if ("delete".equals(request.getParameter("action"))) {
            promotionService.deletePromotion(userBean.getToken(), id);
            session.setAttribute("flash", "The promotion was deleted.");
            response.sendRedirect("home.jsp");
            return;
        }
        errors = FormValidation.validatePromotion(title, content, startDate, endDate);
        if (errors.isEmpty()) {
            PromotionRequest promotionRequest = new PromotionRequest();
            promotionRequest.setTitle(title.trim());
            promotionRequest.setContent(content.trim());
            promotionRequest.setStartDate(startDate);
            promotionRequest.setEndDate(endDate);
            promotionService.updatePromotion(userBean.getToken(), id, promotionRequest);
            session.setAttribute("flash", "The promotion was saved.");
            response.sendRedirect("home.jsp");
            return;
        }
    }
    if (id != null) {
        promotion = promotionService.getPromotionById(userBean.getToken(), id);
        if (errors.isEmpty()) {
            title = promotion.getTitle();
            content = promotion.getContent();
            startDate = promotion.getStartDate().format(FormValidation.DATE_TIME_INPUT);
            endDate = promotion.getEndDate().format(FormValidation.DATE_TIME_INPUT);
        }
    }
} catch (ApiException e) {
    if (e.isSessionExpired()) {
        session.invalidate();
        response.sendRedirect("login.jsp?expired=1");
        return;
    }
    if (e.getStatus() != 404) {
        errors = List.of(e.getMessage());
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
            <a href="home.jsp" class="btn btn-link px-0 mb-2">&larr; Back to overview</a>
            <h1 class="h3 mb-3">Promotion</h1>
            <% if (!errors.isEmpty()) { %>
            <div class="alert alert-danger" role="alert">
                <ul class="mb-0"><% for (String error : errors) { %><li><%= Html.escape(error) %></li><% } %></ul>
            </div>
            <% } %>
            <% if (promotion == null) { %>
            <div class="alert alert-warning">This promotion does not exist.</div>
            <% } else { %>
            <form method="post" action="promotionDetails.jsp?id=<%= id %>" class="card form-card needs-validation" novalidate>
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
                    <button type="submit" name="action" value="delete" class="btn btn-outline-danger"
                            formnovalidate onclick="return confirm('Delete this promotion?');">Delete</button>
                    <button type="submit" name="action" value="update" class="btn btn-primary">Save changes</button>
                </div>
            </form>
            <% } %>
        </div>
    </div>
</main>
<script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/js/forms.js"></script>
</body>
</html>
