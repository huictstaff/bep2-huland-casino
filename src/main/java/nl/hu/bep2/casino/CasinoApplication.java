package nl.hu.bep2.casino;


import nl.hu.bep2.casino.chips.application.ChipsService;

import java.util.Random;

public class CasinoApplication {
    private static String getRandomName(String... names) {
        return names[new Random().nextInt(names.length)];
    }

    public static void main(String[] args) {
        String playerName = getRandomName("Annet", "Hugo", "Pim", "Tom");
        long startKapitaal = 1000;
        ChipsService.instance().depositChips(playerName, startKapitaal);

        System.out.printf("%s betrad het casino, met %s chips op zak%n", playerName, startKapitaal);
        System.out.println("Diens oog viel meteen op de blackjack tafel...");
    }
}
