package com.ccsw.capabilitymanager.exception;

public class ItinerarioVacioException extends RuntimeException {
	public ItinerarioVacioException() {
		super("codigo o name estan vacios");
	}
}
