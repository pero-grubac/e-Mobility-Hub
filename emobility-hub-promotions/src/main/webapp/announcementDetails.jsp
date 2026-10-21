<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.ApiException" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.AnnouncementService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.beans.AnnouncementResponseBean" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.models.dto.request.AnnouncementRequest" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.FormValidation" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.Html" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.beans.UserBean" scope="session" />
<%
String pageTitle = "Announcement";
AnnouncementService announcementService = new AnnouncementService();
List<String> errors = Collections.emptyList();
AnnouncementResponseBean announcement = null;
String title = request.getParameter("title");
String content = request.getParameter("content");

Long id = null;
try {
    id = Long.valueOf(request.getParameter("id"));
} catch (NumberFormatException e) {
    // Shown below as "not found".
}

try {
    if (id != null && "POST".equals(request.getMethod())) {
        if ("delete".equals(request.getParameter("action"))) {
            announcementService.delete(userBean.getToken(), id);
            session.setAttribute("flash", "The announcement was deleted.");
            response.sendRedirect("home.jsp");
            return;
        }
        errors = FormValidation.validateAnnouncement(title, content);
        if (errors.isEmpty()) {
            AnnouncementRequest announcementRequest = new AnnouncementRequest();
            announcementRequest.setTitle(title.trim());
            announcementRequest.setContent(content.trim());
            announcementService.update(userBean.getToken(), id, announcementRequest);
            session.setAttribute("flash", "The announcement was saved.");
            response.sendRedirect("home.jsp");
            return;
        }
    }
    if (id != null) {
        announcement = announcementService.getById(userBean.getToken(), id);
        if (errors.isEmpty()) {
            title = announcement.getTitle();
            content = announcement.getContent();
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
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
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
            <h1 class="h3 mb-3">Announcement</h1>
            <% if (!errors.isEmpty()) { %>
            <div class="alert alert-danger" role="alert">
                <ul class="mb-0"><% for (String error : errors) { %><li><%= Html.escape(error) %></li><% } %></ul>
            </div>
            <% } %>
            <% if (announcement == null) { %>
            <div class="alert alert-warning">This announcement does not exist.</div>
            <% } else { %>
            <form method="post" action="announcementDetails.jsp?id=<%= id %>" class="card shadow-sm needs-validation" novalidate>
                <div class="card-body">
                    <p class="small text-muted">
                        Created <%= announcement.getCreationDate().format(formatter) %> ·
                        updated <%= announcement.getUpdateDate().format(formatter) %>
                    </p>
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" id="title" name="title" required
                               maxlength="<%= FormValidation.TITLE_MAX %>" value="<%= Html.escape(title) %>">
                        <div class="invalid-feedback">A title is required.</div>
                    </div>
                    <div class="mb-3">
                        <label for="content" class="form-label">Content</label>
                        <textarea class="form-control" id="content" name="content" rows="4" required
                                  maxlength="<%= FormValidation.ANNOUNCEMENT_CONTENT_MAX %>"><%= Html.escape(content) %></textarea>
                        <div class="invalid-feedback">Content is required (up to <%= FormValidation.ANNOUNCEMENT_CONTENT_MAX %> characters).</div>
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between">
                    <button type="submit" name="action" value="delete" class="btn btn-outline-danger"
                            formnovalidate onclick="return confirm('Delete this announcement?');">Delete</button>
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
