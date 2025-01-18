<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:useBean id="userBean" class="org.unibl.etf.emobility_hub_promotions.beans.UserBean" scope="session" />
<%@ page import="org.unibl.etf.emobility_hub_promotions.services.PromotionService"%>
<%@ page import="org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest"%>

<%
    if (userBean == null || !userBean.isLoggedIn() || userBean.getToken() == null || userBean.getToken().isEmpty()) {
        response.sendRedirect("login.jsp");
        return;
    }

    String errorMessage = null;

    try {
        if ("create".equals(request.getParameter("action"))) {
            PromotionRequest promotionRequest = new PromotionRequest();
            promotionRequest.setTitle(request.getParameter("title"));
            promotionRequest.setContent(request.getParameter("content"));
            promotionRequest.setStartDate(request.getParameter("startDate").replace(" ", "T"));
            promotionRequest.setEndDate(request.getParameter("endDate").replace(" ", "T"));

            PromotionService promotionService = new PromotionService();
            promotionService.createPromotion(userBean.getToken(), promotionRequest);

            response.sendRedirect("promotions.jsp");
            return;
        }
    } catch (Exception e) {
        errorMessage = "Error: " + e.getMessage();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Create Promotion</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- Datetimepicker CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css">
    <style>
        .logout-btn {
            position: absolute;
            top: 20px;
            right: 20px;
        }
        h1 {
            text-align: center;
            margin-bottom: 30px;
        }
        .navigation-buttons {
            margin-top: 20px;
        }
    </style>
</head>

<body class="bg-light">
    <div class="container mt-5">
        <form action="logout.jsp" method="post" style="display: inline;">
            <button type="submit" class="btn btn-danger logout-btn">Logout</button>
        </form>

        <h1>Create Promotion</h1>

        <% if (errorMessage != null) { %>
            <div class="alert alert-danger"><%= errorMessage %></div>
        <% } %>

        <form method="post">
            <div class="card mt-4 shadow-sm">
                <div class="card-body">
                    <div class="form-group">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" id="title" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="content" class="form-label">Content</label>
                        <textarea class="form-control" id="content" name="content" rows="3" required></textarea>
                    </div>
                    <div class="form-group">
                        <label for="startDate" class="form-label">Start Date</label>
                        <div class='input-group date' id='startDatePicker'>
                            <input type='text' class="form-control" id="startDate" name="startDate" required />
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="endDate" class="form-label">End Date</label>
                        <div class='input-group date' id='endDatePicker'>
                            <input type='text' class="form-control" id="endDate" name="endDate" required />
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between">
                    <button type="submit" name="action" value="create" class="btn btn-primary">Create</button>
                    <button type="button" class="btn btn-secondary" onclick="redirectToPromotions()">Back to Promotions</button>
                </div>
            </div>
        </form>
    </div>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <!-- Moment.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- Datetimepicker JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script>
        $(document).ready(function () {
            $('#startDatePicker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
            });
            $('#endDatePicker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
            });
        });

        function redirectToPromotions() {
            window.location.href = "promotions.jsp";
        }
    </script>
</body>
</html>
