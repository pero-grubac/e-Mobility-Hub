package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.ElectricBicycleDAO;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.ElectricBicycleEntity;

public class ElectricBicycleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	PaginatedResponse<ElectricBicycleEntity> electricBicycles = new PaginatedResponse<ElectricBicycleEntity>();
	private static final Logger logger = Logger.getLogger(ElectricBicycleBean.class.getName());

	public boolean getElectricBicycles(int page, int size) {
		try {
			electricBicycles = null;
			electricBicycles = ElectricBicycleDAO.getElectricBicyclesPaginated(page, size);
			return true;
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

	public double getRentPriceById(long vehicleId) {
		try {
			return ElectricBicycleDAO.getRentPrice(vehicleId);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return 0;
	}

	public boolean brokeBicycle(long id, String description) {
		try {
			return ElectricBicycleDAO.reportFault(id, description);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

	public PaginatedResponse<ElectricBicycleEntity> getElectricBicycles() {
		return electricBicycles;
	}

	public void setElectricBicycles(PaginatedResponse<ElectricBicycleEntity> electricBicycles) {
		this.electricBicycles = electricBicycles;
	}
}
