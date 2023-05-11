package nl.hu.bep2.casino.chips.presentation.dto;

import jakarta.validation.constraints.Positive;

public class Deposit {
    @Positive
    public Long amount;
}
