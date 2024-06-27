package com.ccsw.capabilitymanager.exception;

public class ItinerarioExistenteException extends RuntimeException{
    public ItinerarioExistenteException(String codigo) {
        super("Ya existe un itinerario con el código: " + codigo);
    }
}
