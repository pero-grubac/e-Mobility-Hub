package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.FaultDAO;

public class FaultBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(FaultBean.class.getName());

	public boolean broke(long id, String description) {
		try {
			return FaultDAO.reportFault(id, description);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

}
