package nl.hu.bep2.casino.security.application;

import jakarta.transaction.Transactional;
import nl.hu.bep2.casino.chips.application.ChipsService;
import nl.hu.bep2.casino.security.data.UserRepository;
import nl.hu.bep2.casino.security.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Value("${chips.start-amount}")
    private Long chipsStartAmount;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChipsService chipsService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, ChipsService chipsService) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.chipsService = chipsService;
    }

    public void register(String username, String password, String firstName, String lastName) {
        String encodedPassword = this.passwordEncoder.encode(password);

        User user = new User(username, encodedPassword, firstName, lastName);

        this.userRepository.save(user);
        this.chipsService.depositChips(username, chipsStartAmount);
    }

    @Override
    public User loadUserByUsername(String username) {
        Optional<User> maybeUser = this.userRepository.findByUsername(username);

        if (maybeUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        } else {
            return maybeUser.get();
        }
    }
}
