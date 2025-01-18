package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.RentalDAO;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;

public class RentalBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RentalBean.class.getName());

	public boolean makeRent(RentalEntity entity) {
		try {
			return RentalDAO.createRental(entity);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}
}
