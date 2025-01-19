package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.TransportVehicleDAO;

public class TransportVehicleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TransportVehicleBean.class.getName());

	public double getRentPriceById(long vehicleId) {
		try {
			return TransportVehicleDAO.getRentPrice(vehicleId);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return 0;
	}

	public String imageById(Long id) {
		try {
			return TransportVehicleDAO.getImageById(id);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return null;
	}
}
