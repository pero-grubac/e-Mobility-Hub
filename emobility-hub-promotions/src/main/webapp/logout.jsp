<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%
javax.servlet.http.HttpSession current = request.getSession(false);
if (current != null) {
    current.invalidate();
}
response.sendRedirect("login.jsp");
%>
