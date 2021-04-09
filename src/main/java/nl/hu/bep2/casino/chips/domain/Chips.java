package nl.hu.bep2.casino.chips.domain;

import nl.hu.bep2.casino.chips.domain.exception.NoNegativeDepositException;
import nl.hu.bep2.casino.chips.domain.exception.NotEnoughChipsException;
import nl.hu.bep2.casino.security.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Chips {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    private Long amount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    public Chips() {}
    public Chips(User user, Long amount) {
        this.user = user;
        this.amount = amount;
    }

    public void withdraw(Long amountToWithdraw) {
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

        this.amount = newAmount;
    }

    public void deposit(Long amountToDeposit) {
        if (amountToDeposit < 0) {
            throw new NoNegativeDepositException("Received negative deposit amount: " + amountToDeposit);
        }

        this.amount = this.amount - amountToDeposit;
    }

    public Balance showBalance() {
        return new Balance(
                this.user.getUsername(),
                this.lastUpdate,
                this.amount
        );
    }
}
