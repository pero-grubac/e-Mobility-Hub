package org.unibl.etf.emobility_hub_user.ride;

// A ride could not be started or finished; the message is shown to the client.
public class RideException extends Exception {
	private static final long serialVersionUID = 1L;

	public RideException(String message) {
		super(message);
	}
}
