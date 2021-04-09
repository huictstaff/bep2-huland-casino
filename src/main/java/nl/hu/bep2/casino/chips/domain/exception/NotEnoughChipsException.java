package nl.hu.bep2.casino.chips.domain.exception;

public class NotEnoughChipsException extends RuntimeException {
    public NotEnoughChipsException(String message) {
        super(message);
    }
}
