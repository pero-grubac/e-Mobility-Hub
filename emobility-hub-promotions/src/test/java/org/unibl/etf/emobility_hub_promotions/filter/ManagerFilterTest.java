package org.unibl.etf.emobility_hub_promotions.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unibl.etf.emobility_hub_promotions.beans.UserBean;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

class ManagerFilterTest {
    ManagerFilter filter = new ManagerFilter();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);
    HttpSession session = mock(HttpSession.class);

    @BeforeEach
    void setUp() {
        when(request.getContextPath()).thenReturn("/promotions");
    }

    void signedInAs(String role) {
        UserBean user = new UserBean();
        user.signIn("someone", role, "jwt");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userBean")).thenReturn(user);
    }

    @Test
    void theLoginPageIsOpenToEveryone() throws Exception {
        when(request.getServletPath()).thenReturn("/login.jsp");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(request).setCharacterEncoding("UTF-8");
    }

    @Test
    void aVisitorWithoutASessionIsSentToLogin() throws Exception {
        when(request.getServletPath()).thenReturn("/home.jsp");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/promotions/login.jsp");
        verifyNoInteractions(chain);
    }

    @Test
    void anOperatorIsSentToLogin() throws Exception {
        when(request.getServletPath()).thenReturn("/createPromotion.jsp");
        signedInAs("ROLE_OPERATOR");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/promotions/login.jsp");
        verifyNoInteractions(chain);
    }

    @Test
    void aManagerGetsThrough() throws Exception {
        when(request.getServletPath()).thenReturn("/home.jsp");
        signedInAs(UserBean.MANAGER_ROLE);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
