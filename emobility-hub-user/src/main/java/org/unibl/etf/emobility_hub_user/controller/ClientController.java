package org.unibl.etf.emobility_hub_user.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

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
import org.unibl.etf.emobility_hub_user.beans.ElectricCarBean;
import org.unibl.etf.emobility_hub_user.beans.ElectricScooterBean;
import org.unibl.etf.emobility_hub_user.beans.FaultBean;
import org.unibl.etf.emobility_hub_user.beans.RentalBean;
import org.unibl.etf.emobility_hub_user.beans.TransportVehicleBean;
import org.unibl.etf.emobility_hub_user.invoice.InvoicePdf;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;
import org.unibl.etf.emobility_hub_user.models.entity.DocumentType;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;
import org.unibl.etf.emobility_hub_user.models.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub_user.models.entity.VehicleType;
import org.unibl.etf.emobility_hub_user.ride.RideException;
import org.unibl.etf.emobility_hub_user.storage.AvatarStorage;
import org.unibl.etf.emobility_hub_user.validation.Validators;

// Model 2 front controller: GET renders pages, POST runs an action and redirects.
@MultipartConfig(maxFileSize = 5L * 1024 * 1024, maxRequestSize = 10L * 1024 * 1024, fileSizeThreshold = 512 * 1024)
@WebServlet("/clients")
public class ClientController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ClientController.class.getName());
	private static final String PAGES = "/WEB-INF/pages/";
	private static final int PAGE_SIZE = 6;
	public static final String FLASH_ERROR = "flashError";
	public static final String FLASH_SUCCESS = "flashSuccess";
	public static final String ACTIVE_RIDE = "activeRideId";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");

		if (clientBean == null) {
			forward(request, response, "register".equals(action) ? "register.jsp" : "login.jsp");
			return;
		}

		switch (action == null ? "welcome" : action) {
			case "login", "welcome" -> {
				if (!redirectToActiveRide(request, response, clientBean)) {
					forward(request, response, "welcome.jsp");
				}
			}
			case "car", "bicycle", "scooter" -> showVehicles(request, response, clientBean, action);
			case "rent" -> showRentPage(request, response, clientBean);
			case "ride" -> showRide(request, response, clientBean);
			case "summary" -> showSummary(request, response, clientBean);
			case "invoice" -> sendInvoice(request, response, clientBean);
			case "profile" -> showProfile(request, response, clientBean);
			case "logout" -> {
				session.invalidate();
				response.sendRedirect("clients");
			}
			default -> notFound(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");

		if ("login".equals(action)) {
			handleLogin(request, response);
			return;
		}
		if ("register".equals(action)) {
			handleRegister(request, response);
			return;
		}
		if (clientBean == null) {
			response.sendRedirect("clients");
			return;
		}

		switch (action == null ? "" : action) {
			case "startRide" -> handleStartRide(request, response, clientBean);
			case "finishRide" -> handleFinishRide(request, response, clientBean);
			case "brokenVehicle" -> handleBrokenVehicle(request, response);
			case "changePassword" -> handleChangePassword(request, response, clientBean);
			case "updateAvatar" -> handleUpdateAvatar(request, response, clientBean);
			case "deactivate" -> {
				clientBean.deactivate();
				session.invalidate();
				request.getSession().setAttribute(FLASH_SUCCESS, "Your account has been deactivated.");
				response.sendRedirect("clients");
			}
			default -> notFound(request, response);
		}
	}

	// ── Pages ────────────────────────────────────────────────────────────────

	private void showVehicles(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean,
			String type) throws ServletException, IOException {
		if (redirectToActiveRide(request, response, clientBean)) {
			return;
		}
		int page = pageParam(request);
		switch (type) {
			case "car" -> {
				ElectricCarBean bean = new ElectricCarBean();
				bean.getElectricCars(page, PAGE_SIZE);
				request.setAttribute("vehicles", bean.getElectricCars());
			}
			case "bicycle" -> {
				ElectricBicycleBean bean = new ElectricBicycleBean();
				bean.getElectricBicycles(page, PAGE_SIZE);
				request.setAttribute("vehicles", bean.getElectricBicycles());
			}
			default -> {
				ElectricScooterBean bean = new ElectricScooterBean();
				bean.getElectricScooters(page, PAGE_SIZE);
				request.setAttribute("vehicles", bean.getElectricScooters());
			}
		}
		forward(request, response, type + ".jsp");
	}

	private void showRentPage(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws ServletException, IOException {
		if (redirectToActiveRide(request, response, clientBean)) {
			return;
		}
		TransportVehicleEntity vehicle = vehicleParam(request);
		if (vehicle == null || !vehicle.isAvailable()) {
			flash(request, FLASH_ERROR, "This vehicle is not available any more. Please pick another one.");
			response.sendRedirect("clients?action=" + (vehicle == null ? "welcome" : vehicle.getType().getAction()));
			return;
		}
		request.setAttribute("vehicle", vehicle);
		forward(request, response, "rent.jsp");
	}

	private void showRide(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws ServletException, IOException {
		RentalEntity ride = new RentalBean().getActiveRide(clientBean.getEntity().getId());
		if (ride == null) {
			request.getSession().removeAttribute(ACTIVE_RIDE);
			response.sendRedirect("clients?action=welcome");
			return;
		}
		request.getSession().setAttribute(ACTIVE_RIDE, ride.getId());
		request.setAttribute("ride", ride);
		forward(request, response, "ride.jsp");
	}

	private void showSummary(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws ServletException, IOException {
		RentalEntity rental = completedRentalParam(request, clientBean);
		if (rental == null) {
			notFound(request, response);
			return;
		}
		request.setAttribute("rental", rental);
		forward(request, response, "summary.jsp");
	}

	private void sendInvoice(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws ServletException, IOException {
		RentalEntity rental = completedRentalParam(request, clientBean);
		if (rental == null) {
			notFound(request, response);
			return;
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=invoice_" + rental.getId() + ".pdf");
		try (OutputStream out = response.getOutputStream()) {
			InvoicePdf.write(rental, clientBean.getEntity(), out);
		} catch (Exception e) {
			logger.severe("Invoice " + rental.getId() + " failed: " + e.getMessage());
		}
	}

	private void showProfile(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws ServletException, IOException {
		RentalBean rentalBean = new RentalBean();
		rentalBean.loadRentals(pageParam(request), PAGE_SIZE, clientBean.getEntity().getId());
		request.setAttribute("rentals", rentalBean.getRentals());
		forward(request, response, "profile.jsp");
	}

	// ── Actions ──────────────────────────────────────────────────────────────

	private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ClientBean clientBean = new ClientBean();
		if (!clientBean.login(request.getParameter("username"), request.getParameter("password"))) {
			flash(request, FLASH_ERROR, "Wrong username or password.");
			response.sendRedirect("clients");
			return;
		}
		// A new session on sign-in, so an old session id cannot be reused.
		request.getSession().invalidate();
		HttpSession session = request.getSession(true);
		session.setAttribute("clientBean", clientBean);
		RentalEntity ride = new RentalBean().getActiveRide(clientBean.getEntity().getId());
		if (ride != null) {
			session.setAttribute(ACTIVE_RIDE, ride.getId());
			response.sendRedirect("clients?action=ride");
		} else {
			response.sendRedirect("clients?action=welcome");
		}
	}

	private void handleRegister(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ClientEntity client = new ClientEntity(trim(request.getParameter("username")), request.getParameter("password"),
				trim(request.getParameter("firstName")), trim(request.getParameter("lastName")),
				trim(request.getParameter("email")), trim(request.getParameter("phoneNumber")),
				trim(request.getParameter("idCardNumber")));

		List<String> errors = validateRegistration(client, request.getParameter("confirmPassword"));
		ClientBean clientBean = new ClientBean();
		if (errors.isEmpty() && !clientBean.isUsernameAllowed(client.getUsername())) {
			errors.add("This username is already taken.");
		}
		if (errors.isEmpty()) {
			String taken = clientBean.takenField(client);
			if (taken != null) {
				errors.add("An account with this " + taken + " already exists.");
			}
		}
		Part avatar = request.getPart("avatar");
		if (errors.isEmpty() && AvatarStorage.isPresent(avatar)) {
			try {
				client.setAvatarImage(AvatarStorage.save(avatar, client.getUsername(), request.getContextPath()));
			} catch (IllegalArgumentException e) {
				errors.add(e.getMessage());
			}
		}
		if (errors.isEmpty() && !clientBean.add(client)) {
			errors.add("The account could not be created. Please try again.");
		}

		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			forward(request, response, "register.jsp");
			return;
		}
		flash(request, FLASH_SUCCESS, "Your account is ready. Please sign in.");
		response.sendRedirect("clients");
	}

	static List<String> validateRegistration(ClientEntity client, String confirmPassword) {
		List<String> errors = new ArrayList<>();
		if (!Validators.isValidUsername(client.getUsername())) {
			errors.add("Username must be 3-30 letters, digits, dots or underscores.");
		}
		if (!Validators.isValidPassword(client.getPassword())) {
			errors.add("Password must be at least " + Validators.PASSWORD_MIN_LENGTH + " characters long.");
		} else if (!client.getPassword().equals(confirmPassword)) {
			errors.add("The passwords do not match.");
		}
		if (Validators.isBlank(client.getFirstName()) || Validators.isBlank(client.getLastName())) {
			errors.add("First and last name are required.");
		}
		if (!Validators.isValidEmail(client.getEmail())) {
			errors.add("Enter a valid email address.");
		}
		if (!Validators.isValidPhone(client.getPhoneNumber())) {
			errors.add("Enter a valid phone number, e.g. +387-65-123-456.");
		}
		if (!Validators.isValidIdCard(client.getIdCardNumber())) {
			errors.add("ID card number must be 5-20 letters, digits or dashes.");
		}
		return errors;
	}

	private void handleStartRide(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws IOException {
		TransportVehicleEntity vehicle = vehicleParam(request);
		if (vehicle == null) {
			flash(request, FLASH_ERROR, "Unknown vehicle.");
			response.sendRedirect("clients?action=welcome");
			return;
		}
		String backToForm = "clients?action=rent&vehicleId=" + vehicle.getId();

		RentalEntity ride = new RentalEntity();
		List<String> errors = validateRide(request::getParameter, vehicle, ride, YearMonth.now());
		if (!errors.isEmpty()) {
			flash(request, FLASH_ERROR, String.join(" ", errors));
			response.sendRedirect(backToForm);
			return;
		}
		ride.setClientId(clientBean.getEntity().getId());
		ride.setVehicleId(vehicle.getId());
		ride.setRentalStart(LocalDateTime.now());

		try {
			RentalEntity started = new RentalBean().startRide(ride);
			request.getSession().setAttribute(ACTIVE_RIDE, started.getId());
			flash(request, FLASH_SUCCESS, "Payment confirmed. Enjoy your ride!");
			response.sendRedirect("clients?action=ride");
		} catch (RideException e) {
			flash(request, FLASH_ERROR, e.getMessage());
			response.sendRedirect(backToForm);
		}
	}

	// Fills the ride from the form; the full card number and CVV are only checked, never kept.
	static List<String> validateRide(Function<String, String> param, TransportVehicleEntity vehicle,
			RentalEntity ride, YearMonth now) {
		List<String> errors = new ArrayList<>();
		Double latitude = Validators.parseDouble(param.apply("startLatitude"));
		Double longitude = Validators.parseDouble(param.apply("startLongitude"));
		if (!Validators.isValidLatitude(latitude) || !Validators.isValidLongitude(longitude)) {
			errors.add("Enter a valid pick-up location (latitude -90 to 90, longitude -180 to 180).");
		} else {
			ride.setStartLatitude(latitude);
			ride.setStartLongitude(longitude);
		}

		if (vehicle.getType() == VehicleType.CAR) {
			String documentType = param.apply("documentType");
			String documentNumber = trim(param.apply("documentNumber"));
			String licence = trim(param.apply("driverLicenseNumber"));
			if (!"ID_CARD".equals(documentType) && !"PASSPORT".equals(documentType)) {
				errors.add("Choose your identity document: ID card or passport.");
			} else {
				ride.setDocumentType(DocumentType.valueOf(documentType));
			}
			if (!Validators.isValidDocumentNumber(documentNumber)) {
				errors.add("Enter the document number (5-30 letters, digits or dashes).");
			} else {
				ride.setDocumentNumber(documentNumber);
			}
			if (!Validators.isValidDocumentNumber(licence)) {
				errors.add("Enter your driver's licence number (5-30 letters, digits or dashes).");
			} else {
				ride.setDriverLicenseNumber(licence);
			}
		}

		String cardNumber = param.apply("cardNumber");
		if (Validators.isBlank(param.apply("cardHolder"))) {
			errors.add("Enter the name on the card.");
		}
		if (!Validators.isValidCardNumber(cardNumber)) {
			errors.add("The card number is not valid.");
		} else {
			ride.setPaymentCardLast4(Validators.lastFour(cardNumber));
		}
		if (!Validators.isValidExpiry(param.apply("cardExpiry"), now)) {
			errors.add("The card has expired or the expiry date is not MM/YY.");
		}
		if (!Validators.isValidCvv(param.apply("cardCvv"))) {
			errors.add("The CVV must be 3 or 4 digits.");
		}
		return errors;
	}

	private void handleFinishRide(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws IOException {
		Double latitude = Validators.parseDouble(request.getParameter("endLatitude"));
		Double longitude = Validators.parseDouble(request.getParameter("endLongitude"));
		long rentalId;
		try {
			rentalId = Long.parseLong(request.getParameter("rentalId"));
		} catch (NumberFormatException e) {
			response.sendRedirect("clients?action=ride");
			return;
		}
		if (!Validators.isValidLatitude(latitude) || !Validators.isValidLongitude(longitude)) {
			flash(request, FLASH_ERROR, "Enter a valid drop-off location (latitude -90 to 90, longitude -180 to 180).");
			response.sendRedirect("clients?action=ride");
			return;
		}
		try {
			RentalEntity finished = new RentalBean().finishRide(rentalId, clientBean.getEntity().getId(), latitude,
					longitude);
			request.getSession().removeAttribute(ACTIVE_RIDE);
			response.sendRedirect("clients?action=summary&rentalId=" + finished.getId());
		} catch (RideException e) {
			flash(request, FLASH_ERROR, e.getMessage());
			response.sendRedirect("clients?action=ride");
		}
	}

	private void handleBrokenVehicle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		TransportVehicleEntity vehicle = vehicleParam(request);
		String reason = trim(request.getParameter("reason"));
		String back = "clients?action=" + (vehicle == null ? "welcome" : vehicle.getType().getAction());

		if (vehicle == null || Validators.isBlank(reason) || reason.length() > 1500) {
			flash(request, FLASH_ERROR, "Please describe what is wrong with the vehicle (up to 1500 characters).");
		} else if (new FaultBean().broke(vehicle.getId(), reason)) {
			flash(request, FLASH_SUCCESS, "Thank you. " + vehicle.getModel() + " has been reported as broken.");
		} else {
			flash(request, FLASH_ERROR, "The report could not be saved. Please try again.");
		}
		response.sendRedirect(back);
	}

	private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws IOException {
		String current = request.getParameter("currentPassword");
		String password = request.getParameter("newPassword");
		String confirm = request.getParameter("confirmPassword");

		if (!clientBean.isCurrentPassword(current)) {
			flash(request, FLASH_ERROR, "The current password is not correct.");
		} else if (!Validators.isValidPassword(password)) {
			flash(request, FLASH_ERROR,
					"The new password must be at least " + Validators.PASSWORD_MIN_LENGTH + " characters long.");
		} else if (!password.equals(confirm)) {
			flash(request, FLASH_ERROR, "The new passwords do not match.");
		} else if (clientBean.changePassword(password)) {
			flash(request, FLASH_SUCCESS, "Your password has been changed.");
		} else {
			flash(request, FLASH_ERROR, "The password could not be changed. Please try again.");
		}
		response.sendRedirect("clients?action=profile");
	}

	private void handleUpdateAvatar(HttpServletRequest request, HttpServletResponse response, ClientBean clientBean)
			throws IOException, ServletException {
		Part avatar = request.getPart("avatar");
		if (!AvatarStorage.isPresent(avatar)) {
			flash(request, FLASH_ERROR, "Choose a picture first.");
		} else {
			try {
				String url = AvatarStorage.save(avatar, clientBean.getEntity().getUsername(), request.getContextPath());
				if (clientBean.updateAvatar(url)) {
					flash(request, FLASH_SUCCESS, "Your picture has been updated.");
				} else {
					flash(request, FLASH_ERROR, "The picture could not be saved.");
				}
			} catch (IllegalArgumentException e) {
				flash(request, FLASH_ERROR, e.getMessage());
			}
		}
		response.sendRedirect("clients?action=profile");
	}

	// ── Helpers ──────────────────────────────────────────────────────────────

	// A client with a ride in progress always lands on the ride screen.
	private boolean redirectToActiveRide(HttpServletRequest request, HttpServletResponse response,
			ClientBean clientBean) throws IOException {
		RentalEntity ride = new RentalBean().getActiveRide(clientBean.getEntity().getId());
		if (ride == null) {
			request.getSession().removeAttribute(ACTIVE_RIDE);
			return false;
		}
		request.getSession().setAttribute(ACTIVE_RIDE, ride.getId());
		response.sendRedirect("clients?action=ride");
		return true;
	}

	private TransportVehicleEntity vehicleParam(HttpServletRequest request) {
		try {
			return new TransportVehicleBean().getById(Long.parseLong(request.getParameter("vehicleId")));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// Only the signed-in client's own, finished rentals.
	private RentalEntity completedRentalParam(HttpServletRequest request, ClientBean clientBean) {
		try {
			RentalEntity rental = new RentalBean().getForClient(Long.parseLong(request.getParameter("rentalId")),
					clientBean.getEntity().getId());
			return rental == null || rental.isActive() ? null : rental;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static int pageParam(HttpServletRequest request) {
		try {
			return Math.max(1, Integer.parseInt(request.getParameter("page")));
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	private static String trim(String value) {
		return value == null ? null : value.trim();
	}

	private static void flash(HttpServletRequest request, String key, String message) {
		request.getSession().setAttribute(key, message);
	}

	private static void notFound(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		forward(request, response, "404.jsp");
	}

	private static void forward(HttpServletRequest request, HttpServletResponse response, String page)
			throws ServletException, IOException {
		request.getRequestDispatcher(PAGES + page).forward(request, response);
	}
}
