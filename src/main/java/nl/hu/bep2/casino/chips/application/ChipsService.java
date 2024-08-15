package nl.hu.bep2.casino.chips.application;

import nl.hu.bep2.casino.chips.data.ChipsRepository;
import nl.hu.bep2.casino.chips.domain.Chips;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ChipsService {
    private final ChipsRepository chipsRepository;

    public ChipsService(ChipsRepository chipsRepository) {
        this.chipsRepository = chipsRepository;
    }

    public Balance findBalance(String username) {
        Chips chips = this.findChipsByUsername(username);
        return this.showBalanceFor(chips);
    }

    public Balance depositChips(String username, Long amount) {
        Chips chips = this.findChipsByUsername(username);

        chips.deposit(amount);
        return this.showBalanceFor(chips);
    }

    public Balance withdrawChips(String username, Long amount) {
        Chips chips = this.findChipsByUsername(username);

        chips.withdraw(amount);
        return this.showBalanceFor(chips);
    }

    private Chips findChipsByUsername(String username) {
        Optional<Chips> maybeChips = this.chipsRepository
                .findByUsername(username);

        if (maybeChips.isPresent()) {
            return maybeChips.get();
        } else {
            Chips newChips = new Chips(username, 0L);
            chipsRepository.save(newChips);
            return newChips;
        }
    }

    private Balance showBalanceFor(Chips chips) {
        return new Balance(
                chips.getUsername(),
                chips.getLastUpdate(),
                chips.getAmount()
        );
    }
}
