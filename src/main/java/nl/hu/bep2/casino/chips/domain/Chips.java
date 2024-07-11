package nl.hu.bep2.casino.chips.domain;

import nl.hu.bep2.casino.chips.domain.exception.NegativeNumberException;
import nl.hu.bep2.casino.chips.domain.exception.NotEnoughChipsException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Chips {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private Long amount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    protected Chips() {
    }

    public Chips(String username, Long amount) {
        this.username = username;
        this.amount = amount;
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

        this.amount = newAmount;
    }

    public void deposit(Long amountToDeposit) {
        if (amountToDeposit < 0) {
            throw new NegativeNumberException("Cannot deposit a negative amount: " + amountToDeposit);
        }

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

    public Long getId() {
        return id;
    }

    //Deze methods worden alleen gebruikt in de SqlChipsRepository, die als 'extra voorbeeldje' is meegeleverd
    //Deze code kun je dus in principe verwijderen...
    public static Chips fromDatabaseRow(long id, String username, Long amount, Date creationDate, Date lastUpdate){
        Chips loadedChips = new Chips();
        loadedChips.id = id;
        loadedChips.username = username;
        loadedChips.amount = amount;
        loadedChips.creationDate = creationDate;
        loadedChips.lastUpdate = lastUpdate;
        return loadedChips;
    }

    //Deze methods worden alleen gebruikt in de SqlChipsRepository, die als 'extra voorbeeldje' is meegeleverd
    //Deze code kun je dus in principe verwijderen...
    public void savedInDb(long newId, Date saveDate){
        if(this.id != null){
            throw new IllegalStateException("Cannot set id for Chips " + this.id);
        }
        this.id = newId;
        this.creationDate = saveDate;
        this.lastUpdate = saveDate;
    }

    //Deze methods worden alleen gebruikt in de SqlChipsRepository, die als 'extra voorbeeldje' is meegeleverd
    //Deze code kun je dus in principe verwijderen...
    public void updatedInDb(Date lastUpdate){
        this.lastUpdate = lastUpdate;
    }
}
