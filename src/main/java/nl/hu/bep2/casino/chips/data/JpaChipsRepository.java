package nl.hu.bep2.casino.chips.data;

import jakarta.persistence.EntityManager;
import nl.hu.bep2.casino.chips.domain.Chips;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaChipsRepository implements ChipsRepository {

    private final EntityManager entities;

    public JpaChipsRepository(EntityManager entities) {
        this.entities = entities;
    }

    @Override
    public Optional<Chips> findByUsername(String username) {
        Optional<Chips> result = entities.createQuery("select c from Chips c where c.username = ?1", Chips.class)
                .setParameter(1, username)
                .getResultList().stream().findFirst();

        return result;
    }

    @Override
    public void save(Chips chips) {
        entities.persist(chips);
    }
}
