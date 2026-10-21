package org.unibl.etf.emobility_hub_promotions.filter;

import org.unibl.etf.emobility_hub_promotions.beans.UserBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// Only managers may use this app: every page except login and logout requires
// a signed-in manager. Also makes every request read form fields as UTF-8.
@WebFilter("*.jsp")
public class ManagerFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		request.setCharacterEncoding("UTF-8");

		String path = request.getServletPath();
		if (path.equals("/login.jsp") || path.equals("/logout.jsp")) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = request.getSession(false);
		UserBean user = session == null ? null : (UserBean) session.getAttribute("userBean");
		if (user == null || !user.isManager()) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		chain.doFilter(request, response);
	}
}
