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

    @Test
    void testLedgerCreateAccount() {
        var ledger = new Ledger();
        var account = ledger.createAccount("Alice", new BigDecimal("100"));
        assertNotNull(account);
        assertEquals("Alice", account.getId());
        assertEquals(new BigDecimal("100"), account.getBalance());
    }

    @Test
    void testLedgerCreateAccountZeroBalance() {
        var ledger = new Ledger();
        var account = ledger.createAccount("Bob");
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void testLedgerGetAccount() {
        var ledger = new Ledger();
        ledger.createAccount("Alice", new BigDecimal("100"));
        var account = ledger.getAccount("Alice");
        assertNotNull(account);
        assertEquals("Alice", account.getId());
    }

    @Test
    void testLedgerGetAccountNotFound() {
        var ledger = new Ledger();
        assertNull(ledger.getAccount("Nobody"));
    }

    @Test
    void testDeposit() {
        var ledger = new Ledger();
        var account = ledger.createAccount("Alice", new BigDecimal("100"));
        new Deposit(account, new BigDecimal("50"), ledger).execute();
        assertEquals(new BigDecimal("150"), account.getBalance());
    }

    @Test
    void testWithdrawSuccess() {
        var ledger = new Ledger();
        var account = ledger.createAccount("Bob", new BigDecimal("50"));
        new Withdraw(account, new BigDecimal("20"), ledger).execute();
        assertEquals(new BigDecimal("30"), account.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        var ledger = new Ledger();
        var account = ledger.createAccount("Charlie", new BigDecimal("10"));
        new Withdraw(account, new BigDecimal("50"), ledger).execute();
        assertEquals(new BigDecimal("10"), account.getBalance());
    }
}
