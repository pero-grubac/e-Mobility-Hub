package org.unibl.etf.emobility_hub_user.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.unibl.etf.emobility_hub_user.beans.ClientBean;
import org.unibl.etf.emobility_hub_user.beans.ElectricBicycleBean;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;

@MultipartConfig
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
	    String action = req.getParameter("action");
	    if ("brokenVehicle".equals(action)) {
	        handleBrokenVehicle(req, resp);
	    } else {
	        resp.sendRedirect("clients?action=bicycle");
	    }
	}

	private void handleBrokenVehicle(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    HttpSession session = request.getSession();
	    ElectricBicycleBean bicycleBean = (ElectricBicycleBean) session.getAttribute("bicycleBean");
	    if (bicycleBean == null) {
	        bicycleBean = new ElectricBicycleBean();
	        session.setAttribute("bicycleBean", bicycleBean);
	    }

	    String idParam = request.getParameter("id");
	    String reason = request.getParameter("reason");

	    if (idParam == null || reason == null || reason.trim().isEmpty()) {
	        session.setAttribute("errorMessage", "Invalid request parameters.");
	        response.sendRedirect("clients?action=bicycle");
	        return;
	    }

	    try {
	        long bicycleId = Long.parseLong(idParam);
	        boolean isBrokenReported = bicycleBean.brokeBicycle(bicycleId, reason.trim());

	        if (isBrokenReported) {
	            session.setAttribute("successMessage", "Bicycle reported as broken successfully.");
	        } else {
	            session.setAttribute("errorMessage", "Failed to report broken bicycle.");
	        }
	    } catch (NumberFormatException e) {
	        session.setAttribute("errorMessage", "Invalid bicycle ID.");
	    } catch (Exception e) {
	        session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
	        e.printStackTrace();
	    }

	    response.sendRedirect("clients?action=bicycle");
	}


	

	private String getActionFromMultipartRequest(HttpServletRequest request) throws Exception {
		String action = null;

		boolean isMultipart = org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {
			org.apache.commons.fileupload.disk.DiskFileItemFactory factory = new org.apache.commons.fileupload.disk.DiskFileItemFactory();
			org.apache.commons.fileupload.servlet.ServletFileUpload upload = new org.apache.commons.fileupload.servlet.ServletFileUpload(
					factory);

			java.util.List<org.apache.commons.fileupload.FileItem> items = upload.parseRequest(request);

			for (org.apache.commons.fileupload.FileItem item : items) {
				if (item.isFormField() && "action".equals(item.getFieldName())) {
					action = item.getString();
					break;
				}
			}
		}

		return action;
	}

	private String handleAction(HttpServletRequest request, HttpServletResponse response, String action) {
		HttpSession session = request.getSession();
		session.setAttribute("notification", "");

		if (action == null || action.isEmpty()) {
			try {
				action = getActionFromMultipartRequest(request);
			} catch (Exception e) {
				e.printStackTrace();
				return "/WEB-INF/pages/404.jsp";
			}

		}

		if (action == null || action.isEmpty()) {
			return "/WEB-INF/pages/login.jsp";
		}

		try {
			switch (action) {
			case "login":
				return handleLogin(request, session);
			case "register":
				return handleRegister(request, session);
			case "welcome":
				return handleWelcome(session);
			case "logout":
				return handleLogout(session);
			case "profile":
				return hadnleProfile(session);
			case "changePassword":
				return handleChangePassword(request, session);
			case "deactivate":
				return handleDeactivate(session);
			case "updateAvatar":
				return handleUpdateAvatar(request, session);
			case "bicycle":
				return handlebicycle(request, session);
			case "brokenVehicle":
				return handleBrokenVehicle(request, response, session);
			default:
				return "/WEB-INF/pages/404.jsp";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "/WEB-INF/pages/404.jsp";
		}

	}

	private String handleBrokenVehicle(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		ElectricBicycleBean bicycleBean = new ElectricBicycleBean();

		// Dohvati parametre iz zahteva
		String idParam = request.getParameter("id");
		String reason = request.getParameter("reason");

		// Validacija parametara
		if (idParam == null || reason == null || reason.trim().isEmpty()) {
			session.setAttribute("errorMessage", "Invalid request parameters.");
			response.sendRedirect("clients?action=bicycle"); // Redirekcija na listu bicikala
			return null;
		}

		try {
			long bicycleId = Long.parseLong(idParam);

			// Pozovi metodu za prijavljivanje greške
			boolean isBrokenReported = bicycleBean.brokeBicycle(bicycleId, reason.trim());

			if (isBrokenReported) {
				session.setAttribute("successMessage", "Bicycle reported as broken successfully.");
			} else {
				session.setAttribute("errorMessage", "Failed to report broken bicycle.");
			}
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Invalid bicycle ID.");
		} catch (Exception e) {
			session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}

		// Redirekcija na listu bicikala
		response.sendRedirect("clients?action=bicycle");
		return null; // Ne vraćamo JSP direktno
	}

	private String handlebicycle(HttpServletRequest request, HttpSession session) {

		ElectricBicycleBean bicycleBean = new ElectricBicycleBean();
		int page = 1; // Default page
		int size = 6; // Default size (6 bicycles per page)

		String pageParam = request.getParameter("page");
		if (pageParam != null && !pageParam.isEmpty()) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if (bicycleBean.getElectricBicycles(page, size)) {
			session.setAttribute("bicycleBean", bicycleBean);
			session.setAttribute("currentPage", page);
		} else {
			session.setAttribute("notification", "Failed to load bicycles.");
		}

		return "/WEB-INF/pages/bicycle.jsp";
	}

	private String handleUpdateAvatar(HttpServletRequest request, HttpSession session) {
		ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");

		if (clientBean != null) {
			try {
				boolean isMultipart = org.apache.commons.fileupload.servlet.ServletFileUpload
						.isMultipartContent(request);

				if (isMultipart) {
					Part file = request.getPart("avatar");
					String originalFileName = file.getSubmittedFileName();
					String extension = "";

					if (originalFileName != null && originalFileName.contains(".")) {
						extension = originalFileName.substring(originalFileName.lastIndexOf("."));
					}

					String fileName = UUID.randomUUID().toString() + extension;

					String relativePath = "images" + java.io.File.separator + clientBean.getEntity().getUsername();
					String uploadDir = getServletContext().getRealPath("/") + relativePath;

					java.io.File dir = new java.io.File(uploadDir);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String uploadPath = uploadDir + java.io.File.separator + fileName;
					System.out.println("Saving file to: " + uploadPath);

					try (FileOutputStream fos = new FileOutputStream(uploadPath);
							InputStream is = file.getInputStream()) {
						byte[] data = new byte[is.available()];
						is.read(data);
						fos.write(data);
					}
					String baseUrl = request.getScheme() + "://" + // Protokol (http ili https)
							request.getServerName() + // Ime servera (localhost ili domen)
							":" + request.getServerPort() + // Port (8081)
							request.getContextPath() + "/"; // Kontekst aplikacije (/eMobilityHubUser)

					String dbPath = baseUrl + relativePath + "/" + fileName;
					dbPath = dbPath.replace("\\", "/");

					if (clientBean.updateAvater(dbPath)) {
						session.setAttribute("notification", "Avatar updated successfully.");
					} else {
						session.setAttribute("notification", "Failed to update avatar.");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				session.setAttribute("notification", "Error while uploading file.");
			}
		}

		return "/WEB-INF/pages/profile.jsp";
	}

	private String handleDeactivate(HttpSession session) {
		ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");

		if (clientBean != null) {
			clientBean.deactivate();
			session.invalidate();
			return "/WEB-INF/pages/login.jsp";
		}

		return "/WEB-INF/pages/404.jsp";
	}

	private String handleChangePassword(HttpServletRequest request, HttpSession session) {
		String newPassword = request.getParameter("password");
		ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");

		if (clientBean != null && clientBean.changePassword(newPassword)) {
			session.setAttribute("notification", "Password updated successfully.");
		} else {
			session.setAttribute("notification", "Failed to update password.");
		}

		return "/WEB-INF/pages/profile.jsp";
	}

	private String hadnleProfile(HttpSession session) {
		return "/WEB-INF/pages/profile.jsp";
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
