package org.unibl.etf.emobility_hub_user.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.dao.ClientDAO;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;

public class ClientBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private ClientEntity entity = new ClientEntity();
	private boolean isLoggedIn = false;
	private static final Logger logger = Logger.getLogger(ClientBean.class.getName());

	public boolean login(String username, String password) {
		try {
			if ((entity = ClientDAO.getByUsernameAndPassword(username, password)) != null) {
				isLoggedIn = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isLoggedIn;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void logout() {
		entity = new ClientEntity();
		isLoggedIn = false;
	}

	public boolean add(ClientEntity client) {
		try {
			return ClientDAO.createClient(client);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

	public boolean isUsernameAllowed(String username) {
		try {
			return !ClientDAO.usernameExists(username);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

	public boolean changePassword(String password) {
		try {
			return ClientDAO.updatePassword(entity.getId(), password);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

	public void deactivate() {
		try {
			ClientDAO.updateIsDeactivated(entity.getId(), true);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	public boolean updateAvater(String path) {
		try {
			if (ClientDAO.updateAvatar(entity.getId(), path)) {
				entity.setAvatarImage(path);
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return false;
	}

	public ClientEntity getEntity() {
		return entity;
	}

	public void setEntity(ClientEntity entity) {
		this.entity = entity;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
}
