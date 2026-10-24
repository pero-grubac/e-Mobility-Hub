<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.ApiException" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.AnnouncementService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.models.dto.request.AnnouncementRequest" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.FormValidation" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.util.Html" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.beans.UserBean" scope="session" />
<%
String pageTitle = "New announcement";
String title = request.getParameter("title");
String content = request.getParameter("content");
List<String> errors = Collections.emptyList();

if ("POST".equals(request.getMethod())) {
    errors = FormValidation.validateAnnouncement(title, content);
    if (errors.isEmpty()) {
        try {
            AnnouncementRequest announcementRequest = new AnnouncementRequest();
            announcementRequest.setTitle(title.trim());
            announcementRequest.setContent(content.trim());
            new AnnouncementService().create(userBean.getToken(), announcementRequest);
            session.setAttribute("flash", "Announcement \"" + title.trim() + "\" was published.");
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
            <h1 class="h3 mb-3">New announcement</h1>
            <% if (!errors.isEmpty()) { %>
            <div class="alert alert-danger" role="alert">
                <ul class="mb-0"><% for (String error : errors) { %><li><%= Html.escape(error) %></li><% } %></ul>
            </div>
            <% } %>
            <form method="post" action="createAnnouncement.jsp" class="card form-card needs-validation" novalidate id="announcement-form">
                <div class="card-body">
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
                    <a class="btn btn-outline-secondary" href="home.jsp">Cancel</a>
                    <button type="submit" class="btn btn-primary" id="save-announcement">Publish announcement</button>
                </div>
            </form>
        </div>
    </div>
</main>
<script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/js/forms.js"></script>
</body>
</html>
