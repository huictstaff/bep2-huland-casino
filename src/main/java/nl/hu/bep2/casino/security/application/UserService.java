package nl.hu.bep2.casino.security.application;

import nl.hu.bep2.casino.chips.data.Chips;
import nl.hu.bep2.casino.chips.data.SpringChipsRepository;
import nl.hu.bep2.casino.security.data.User;
import nl.hu.bep2.casino.security.data.SpringUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 *  Implements UserDetailsService in order to make it usable
 *  as login/registration service for Spring.
 *  (see AuthenticationFilter)
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    @Value("${chips.start-amount}")
    private Long chipsStartAmount;

    private final SpringUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpringChipsRepository chipsRepository;


    public UserService(SpringUserRepository repository, PasswordEncoder passwordEncoder, SpringChipsRepository chipsRepository) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.chipsRepository = chipsRepository;
    }

    public void register(String username, String password, String firstName, String lastName) {
        String encodedPassword = this.passwordEncoder.encode(password);

        User user = new User(username, encodedPassword, firstName, lastName);
        Chips chips = new Chips(user, chipsStartAmount);

        this.userRepository.save(user);
        this.chipsRepository.save(chips);
    }

    @Override
    public User loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
