package nl.hu.bep2.casino.chips.domain.exception;

public class NegativeNumberException extends RuntimeException {
    public NegativeNumberException(String message) {
        super(message);
    }
}
