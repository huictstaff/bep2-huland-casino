package nl.hu.bep2.casino.chips.data;

import nl.hu.bep2.casino.chips.domain.Chips;
import java.util.Optional;

public interface ChipsRepository {
    Optional<Chips> findByUsername(String username);
    void save(Chips chips);
}
