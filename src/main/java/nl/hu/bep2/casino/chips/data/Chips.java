package nl.hu.bep2.casino.chips.data;

import nl.hu.bep2.casino.security.data.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This is a data model.
 *
 * It is similar to a domain model, but is
 * intended for storage purposes. It does not
 * contain a lot of business logic.
 */
@Entity
@Table(name = "chips")
public class Chips {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @Column
    private Long amount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date creationDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date lastUpdate;

    public Chips() {}
    public Chips(User user, Long amount) {
        this.user = user;
        this.amount = amount;
    }

    public void deposit(Long amount) {
        this.amount += amount;
    }

    public User getUser() {
        return user;
    }

    public Long getAmount() {
        return amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }
}
