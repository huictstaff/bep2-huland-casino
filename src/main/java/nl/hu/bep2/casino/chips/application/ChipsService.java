package nl.hu.bep2.casino.chips.application;

import nl.hu.bep2.casino.chips.domain.Chips;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChipsService {
    private final ArrayList<Chips> chips = new ArrayList<>();

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
        for (Chips c : this.chips) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }

        Chips chipsForUnknownUser = new Chips(username, 0L);
        this.chips.add(chipsForUnknownUser);
        return chipsForUnknownUser;
    }

    private Balance showBalanceFor(Chips chips) {
        return new Balance(
                chips.getUsername(),
                chips.getLastUpdate(),
                chips.getAmount()
        );
    }
}
