package org.unibl.etf.emobility_hub_user.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.unibl.etf.emobility_hub_user.beans.ClientBean;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;

@WebServlet("/clients")
public class ClientController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ClientController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String address = handleAction(request, response, action);
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	private String handleAction(HttpServletRequest request, HttpServletResponse response, String action) {
		HttpSession session = request.getSession();
		session.setAttribute("notification", "");

		if (action == null || action.isEmpty()) {
			return "/WEB-INF/pages/login.jsp";
		}

		switch (action) {
		case "login":
			return handleLogin(request, session);
		case "register":
			return handleRegister(request, session);
		case "welcome":
			return handleWelcome(session);
		case "logout":
			return handleLogout(session);
		default:
			return "/WEB-INF/pages/404.jsp";
		}
	}

	private String handleLogin(HttpServletRequest request, HttpSession session) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		ClientBean clientBean = new ClientBean();

		if (clientBean.login(username, password)) {
			session.setAttribute("clientBean", clientBean);
			return "/WEB-INF/pages/welcome.jsp";
		} else {
			session.setAttribute("notification", "Wrong credentials");
			return "/WEB-INF/pages/login.jsp";
		}
	}

	private String handleRegister(HttpServletRequest request, HttpSession session) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		String idCardNumber = request.getParameter("idCardNumber");

		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			return "/WEB-INF/pages/register.jsp";
		}

		ClientEntity entity = new ClientEntity(username, password, firstName, lastName, email, phoneNumber,
				idCardNumber);
		ClientBean clientBean = new ClientBean();

		try {
			if (clientBean.isUsernameAllowed(username)) {
				if (clientBean.add(entity)) {
					return "/WEB-INF/pages/login.jsp";
				} else {
					session.setAttribute("notification", "Username exists");
				}
			}
		} catch (Exception e) {
			session.setAttribute("notification", "ERROR: " + e.getMessage());
		}

		return "/WEB-INF/pages/register.jsp";
	}

	private String handleWelcome(HttpSession session) {
		ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");
		if (clientBean == null || !clientBean.isLoggedIn()) {
			return "/WEB-INF/pages/login.jsp";
		}
		return "/WEB-INF/pages/welcome.jsp";
	}

	private String handleLogout(HttpSession session) {
		session.invalidate();
		return "/WEB-INF/pages/login.jsp";
	}
}
