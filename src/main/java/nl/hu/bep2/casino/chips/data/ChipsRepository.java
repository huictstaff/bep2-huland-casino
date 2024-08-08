package nl.hu.bep2.casino.chips.data;

import nl.hu.bep2.casino.chips.domain.Chips;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChipsRepository {
    Optional<Chips> findByUsername(String username);
    void save(Chips chips);
}
