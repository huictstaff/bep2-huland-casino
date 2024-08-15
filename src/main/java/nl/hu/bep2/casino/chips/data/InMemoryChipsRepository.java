package nl.hu.bep2.casino.chips.data;

import nl.hu.bep2.casino.chips.domain.Chips;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class InMemoryChipsRepository implements ChipsRepository {

    private final ArrayList<Chips> entities = new ArrayList<>();

    public InMemoryChipsRepository(){
    }

    @Override
    public Optional<Chips> findByUsername(String username) {
        for(Chips chips: this.entities){
            if(chips.getUsername().equals(username)){
                return Optional.of(chips);
            }
        }

        return Optional.empty();
    }

    @Override
    public void save(Chips chips) {
        entities.add(chips);
    }
}
