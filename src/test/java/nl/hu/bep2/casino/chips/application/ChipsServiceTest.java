package nl.hu.bep2.casino.chips.application;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ChipsServiceTest {

    @Test
    public void balanceStartsOffZero(){
        assertEquals(0, ChipsService.instance().findBalance("Anton").getChips());
    }

    @Test
    public void canDepositCash(){
        ChipsService.instance().depositChips("Bob", 100L);
        assertEquals(100L, ChipsService.instance().findBalance("Bob").getChips());
    }

    @Test
    public void canWithdrawCash(){
        ChipsService.instance().depositChips("Cleo", 100L);
        ChipsService.instance().withdrawChips("Cleo", 50L);
        assertEquals(50L, ChipsService.instance().findBalance("Cleo").getChips());
    }
}