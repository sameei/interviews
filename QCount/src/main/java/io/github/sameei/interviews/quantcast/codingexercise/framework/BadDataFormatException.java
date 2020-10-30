package io.github.sameei.interviews.quantcast.codingexercise.framework;

public class BadDataFormatException extends RuntimeException {
    public BadDataFormatException(String message) {
        super(message);
    }

    public BadDataFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BadDataFormatException format(String format, Object ... args) {
        String message = String.format(format, args);
        return new BadDataFormatException(message);
    }

    public static BadDataFormatException format(Throwable cause, String format, Object ... args) {
        String message = String.format(format, args);
        return new BadDataFormatException(message, cause);
    }
}
