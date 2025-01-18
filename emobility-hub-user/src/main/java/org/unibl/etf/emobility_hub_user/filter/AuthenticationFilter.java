package org.unibl.etf.emobility_hub_user.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		String action = httpRequest.getParameter("action");
		String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

		if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
			chain.doFilter(request, response);
			return;
		}
		boolean isLoggedIn = (session != null && session.getAttribute("clientBean") != null);
		boolean isLoginOrRegister = (action == null || action.equals("login") || action.equals("register"));

		if (!isLoggedIn && !isLoginOrRegister) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/clients?action=login");
			return;
		}

		chain.doFilter(request, response);
	}

}
