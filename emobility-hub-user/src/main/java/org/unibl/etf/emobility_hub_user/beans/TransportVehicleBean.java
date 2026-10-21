package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.TransportVehicleDAO;
import org.unibl.etf.emobility_hub_user.models.entity.TransportVehicleEntity;

public class TransportVehicleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TransportVehicleBean.class.getName());

	public TransportVehicleEntity getById(long vehicleId) {
		try {
			return TransportVehicleDAO.getById(vehicleId);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return null;
	}
}
