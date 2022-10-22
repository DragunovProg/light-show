package ua.dragunov.lightshow.exceptions;

public class LightShowException extends Exception{

    public LightShowException(String message) {
        super(message);
    }

    public LightShowException(String message, Throwable cause) {
        super(message, cause);
    }

    public LightShowException(Throwable cause) {
        super(cause);
    }
}
