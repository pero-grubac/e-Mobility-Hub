<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.AnnouncementService" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse" %>
<%@ page import="org.unibl.etf.emobility_hub_promotions.beans.AnnouncementResponseBean" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.beans.UserBean" scope="session" />
<%
if (userBean == null || !userBean.isLoggedIn() || userBean.getToken() == null || userBean.getToken().isEmpty()) {
    response.sendRedirect("login.jsp");
    return;
}

String searchQuery = request.getParameter("search");
int pageNum = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 0;
int size = 6;
List<AnnouncementResponseBean> announcements = null;
int totalPages = 0;
String errorMessage = null;

try {
    AnnouncementService service = new AnnouncementService();
    PaginatedResponse<AnnouncementResponseBean> paginatedResponse = service.getAnnoucements(userBean.getToken(), pageNum, size, searchQuery);
    if (paginatedResponse != null) {
        announcements = paginatedResponse.getContent();
        totalPages = paginatedResponse.getPage().getTotalPages();
    }
} catch (Exception e) {
    System.err.println("Announcements error: " + e.getMessage());
    e.printStackTrace();
    errorMessage = "Error: " + e.getMessage();
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Announcements</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center">
        <h1>Announcements</h1>
        <div>
            <button class="btn btn-info" onclick="redirectToPromotions()">Promotions</button>
            <button class="btn btn-primary" onclick="redirectToCreate()">Create Announcement</button>
            <form action="logout.jsp" method="post" style="display: inline;">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
    </div>

    <form method="get" class="mt-3" id="searchForm">
        <div class="input-group">
            <input type="text" class="form-control" name="search"
                placeholder="Search by content"
                value='<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>'>
            <div class="input-group-append">
                <button class="btn btn-outline-primary" type="submit">Search</button>
            </div>
        </div>
    </form>

    <% if (errorMessage != null) { %>
    <div class="alert alert-danger"><%= errorMessage %></div>
    <% } %>

    <div class="row mt-4">
        <% if (announcements != null && !announcements.isEmpty()) { %>
        <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
           for (AnnouncementResponseBean announcement : announcements) { %>
        <div class="col-md-4 mb-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title"><%= announcement.getTitle() %></h5>
                    <p class="card-text">
                        <% String shortContent = announcement.getContent().length() > 30
                            ? announcement.getContent().substring(0, 30) + "..."
                            : announcement.getContent(); %>
                        <%= shortContent %>
                    </p>
                    <p class="text-muted">Created: <%= announcement.getCreationDate().format(formatter) %></p>
                    <p class="text-muted">Updated: <%= announcement.getUpdateDate().format(formatter) %></p>
                    <button class="btn btn-primary" onclick="redirectToDetails('<%= announcement.getId() %>')">Details</button>
                </div>
            </div>
        </div>
        <% } %>
        <% } else { %>
        <div class="alert alert-warning">No announcements found.</div>
        <% } %>
    </div>

    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <% for (int i = 0; i < totalPages; i++) { %>
            <li class="page-item <%= (i == pageNum) ? "active" : "" %>">
                <a class="page-link" href="announcements.jsp?page=<%= i %>&size=<%= size %>"><%= i + 1 %></a>
            </li>
            <% } %>
        </ul>
    </nav>
</div>

<script>
    function redirectToDetails(id) { window.location.href = "announcementDetails.jsp?id=" + id; }
    function redirectToCreate() { window.location.href = "createAnnouncement.jsp"; }
    function redirectToPromotions() { window.location.href = "promotions.jsp"; }
</script>
<script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
</body>
</html>