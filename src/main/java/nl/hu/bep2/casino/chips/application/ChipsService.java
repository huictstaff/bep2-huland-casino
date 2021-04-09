package nl.hu.bep2.casino.chips.application;

import nl.hu.bep2.casino.chips.data.SpringChipsRepository;
import nl.hu.bep2.casino.chips.domain.Balance;
import nl.hu.bep2.casino.chips.domain.Chips;
import nl.hu.bep2.casino.security.application.UserService;
import nl.hu.bep2.casino.security.domain.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class ChipsService {
    private final UserService userService;
    private final SpringChipsRepository chipsRepository;

    public ChipsService(UserService userService, SpringChipsRepository chipsRepository) {
        this.userService = userService;
        this.chipsRepository = chipsRepository;
    }

    public Balance findBalance(String username) {
        Chips chips = this.findChipsByUsername(username);
        return chips.showBalance();
    }

    /**
     * Deposits any amount of chips.
     * In a real world scenario, you would send the
     * user through a payment gateway before adding the amount.
     */
    public Balance depositChips(String username, Long amount) {
        Chips chips = this.findChipsByUsername(username);

        chips.deposit(amount);
        this.chipsRepository.save(chips);

        return chips.showBalance();
    }

    public Balance withdrawChips(String username, Long amount) {
        Chips chips = this.findChipsByUsername(username);

        chips.withdraw(amount);
        this.chipsRepository.save(chips);

        return chips.showBalance();
    }

    public Chips findChipsByUsername(String username) {
        User user = this.userService.loadUserByUsername(username);

        return this.chipsRepository
                .findByUser(user)
                .orElse(new Chips(user, 0L));
    }
}
