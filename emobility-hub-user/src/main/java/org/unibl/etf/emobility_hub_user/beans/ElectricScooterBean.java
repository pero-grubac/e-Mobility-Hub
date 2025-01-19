package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.ElectricScooterDAO;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.ElectricScooterEntity;

public class ElectricScooterBean implements Serializable {
	private static final long serialVersionUID = 1L;
	PaginatedResponse<ElectricScooterEntity> electricScooters = new PaginatedResponse<ElectricScooterEntity>();
	private static final Logger logger = Logger.getLogger(ElectricScooterBean.class.getName());

	public boolean getElectricScooters(int page, int size) {
		try {
			electricScooters = null;
			electricScooters = ElectricScooterDAO.getElectricScootersPaginated(page, size);
			return true;
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		return false;
	}

	public PaginatedResponse<ElectricScooterEntity> getElectricScooters() {
		return electricScooters;
	}

	public void setElectricScooters(PaginatedResponse<ElectricScooterEntity> electricScooters) {
		this.electricScooters = electricScooters;
	}


}
