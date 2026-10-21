package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.RentalDAO;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;
import org.unibl.etf.emobility_hub_user.ride.RideException;

public class RentalBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RentalBean.class.getName());
	private PaginatedResponse<RentalEntity> rentals = new PaginatedResponse<RentalEntity>();

	public RentalEntity startRide(RentalEntity ride) throws RideException {
		return RentalDAO.startRide(ride);
	}

	public RentalEntity finishRide(long rentalId, long clientId, double endLatitude, double endLongitude)
			throws RideException {
		return RentalDAO.finishRide(rentalId, clientId, endLatitude, endLongitude, LocalDateTime.now());
	}

	public RentalEntity getActiveRide(long clientId) {
		try {
			return RentalDAO.getActiveRide(clientId);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return null;
	}

	public RentalEntity getForClient(long rentalId, long clientId) {
		try {
			return RentalDAO.getByIdForClient(rentalId, clientId);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return null;
	}

	public boolean loadRentals(int page, int size, long clientId) {
		try {
			rentals = RentalDAO.getPaginatedRentalsByClient(page, size, clientId);
			return true;
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

	public PaginatedResponse<RentalEntity> getRentals() {
		return rentals;
	}
}
