package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.ElectricCarDAO;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.ElectricCarEntity;

public class ElectricCarBean implements Serializable {
	private static final long serialVersionUID = 1L;
	PaginatedResponse<ElectricCarEntity> electricCars = new PaginatedResponse<ElectricCarEntity>();
	private static final Logger logger = Logger.getLogger(ElectricCarBean.class.getName());

	public boolean getElectricBicycles(int page, int size) {
		try {
			electricCars = null;
			electricCars = ElectricCarDAO.getElectricCarsPaginated(page, size);
			return true;
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		return false;
	}

	public PaginatedResponse<ElectricCarEntity> getElectricBicycles() {
		return electricCars;
	}

	public void setElectricBicycles(PaginatedResponse<ElectricCarEntity> electricBicycles) {
		this.electricCars = electricBicycles;
	}

}
