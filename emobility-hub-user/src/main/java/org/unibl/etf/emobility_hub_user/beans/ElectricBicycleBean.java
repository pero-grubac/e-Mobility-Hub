package org.unibl.etf.emobility_hub_user.beans;

import org.unibl.etf.emobility_hub_user.dao.ElectricBicycleDAO;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.ElectricBicycleEntity;

import java.io.Serializable;
import java.util.logging.Logger;

public class ElectricBicycleBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ElectricBicycleBean.class.getName());
    PaginatedResponse<ElectricBicycleEntity> electricBicycles = new PaginatedResponse<ElectricBicycleEntity>();

    public boolean getElectricBicycles(int page, int size) {
        try {
            electricBicycles = ElectricBicycleDAO.getElectricBicyclesPaginated(page, size);
            return true;
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        return false;
    }

    public PaginatedResponse<ElectricBicycleEntity> getElectricBicycles() {
        return electricBicycles;
    }
}
