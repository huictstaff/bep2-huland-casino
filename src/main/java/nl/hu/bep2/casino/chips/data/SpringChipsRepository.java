package nl.hu.bep2.casino.chips.data;

import nl.hu.bep2.casino.chips.domain.Chips;
import nl.hu.bep2.casino.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This is a magic interface, which is automatically implemented
 * by Spring based on the chosen data storage configuration.
 */
public interface SpringChipsRepository extends JpaRepository<Chips, Long> {
    Optional<Chips> findByUser(User user);
}
