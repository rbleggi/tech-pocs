package com.rbleggi.corebankledger;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CoreBankLedgerTest {

    @Test
    void testAccountCreation() {
        var account = new Account("Alice", new BigDecimal("100"));
        assertEquals("Alice", account.getId());
        assertEquals(new BigDecimal("100"), account.getBalance());
    }

    @Test
    void testAccountSetBalance() {
        var account = new Account("Bob", new BigDecimal("50"));
        account.setBalance(new BigDecimal("200"));
        assertEquals(new BigDecimal("200"), account.getBalance());
    }
}
