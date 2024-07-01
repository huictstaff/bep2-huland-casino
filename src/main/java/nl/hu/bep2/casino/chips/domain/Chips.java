package nl.hu.bep2.casino.chips.domain;

import nl.hu.bep2.casino.chips.domain.exception.NegativeNumberException;
import nl.hu.bep2.casino.chips.domain.exception.NotEnoughChipsException;

import java.util.Date;

public class Chips {
    private String username;

    private Long amount;

    private Date creationDate;

    private Date lastUpdate;

    protected Chips() {
    }

    public Chips(String username, Long amount) {
        this.username = username;
        this.amount = amount;
        this.creationDate = new Date();
    }

    public void withdraw(Long amountToWithdraw) {
        if (amountToWithdraw < 0) {
            throw new NegativeNumberException("Cannot withdraw a negative amount: " + amountToWithdraw);
        }

        long newAmount = this.amount - amountToWithdraw;

        if (newAmount < 0) {
            throw new NotEnoughChipsException(
                    String.format(
                            "Cannot withdraw %d chips: %d chips remaining",
                            amountToWithdraw,
                            this.amount
                    )
            );
        }

        this.lastUpdate = new Date();
        this.amount = newAmount;

    }

    public void deposit(Long amountToDeposit) {
        if (amountToDeposit < 0) {
            throw new NegativeNumberException("Cannot deposit a negative amount: " + amountToDeposit);
        }

        this.lastUpdate = new Date();
        this.amount = this.amount + amountToDeposit;
    }

    public String getUsername() {
        return username;
    }

    public Long getAmount() {
        return amount;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }
}
