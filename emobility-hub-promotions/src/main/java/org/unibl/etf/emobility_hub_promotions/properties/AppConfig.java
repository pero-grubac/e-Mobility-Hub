package org.unibl.etf.emobility_hub_promotions.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
	private Properties properties;

	public AppConfig() {
		properties = new Properties();
		loadProperties();
	}

	private void loadProperties() {
		try (InputStream input = getClass().getClassLoader()
				.getResourceAsStream("org/unibl/etf/emobility_hub_promotions/properties/app.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find app.properties");
				return;
			}
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// LOGIN_URL, PROMOTIONS_URL and ANNOUNCEMENTS_URL can be set as environment
	// variables (Docker does); app.properties holds the local defaults.
	private String getProperty(String key) {
		String env = System.getenv(key);
		return env != null && !env.isEmpty() ? env : properties.getProperty(key);
	}



	public String getLoginUrl() {
		return getProperty("LOGIN_URL");
	}

	public String getPromotionsUrl() {
		return getProperty("PROMOTIONS_URL");
	}

	public String getAnnouncementsUrl() {
		return getProperty("ANNOUNCEMENTS_URL");
	}
}
