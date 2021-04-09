package nl.hu.bep2.casino.chips.domain.exception;

public class NoNegativeDepositException extends RuntimeException {
    public NoNegativeDepositException(String message) {
        super(message);
    }
}
