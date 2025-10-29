package com.rbleggi.corebankledger

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CoreBankLedgerTest {
    @Test
    fun `Ledger should create account with initial balance`() {
        val ledger = Ledger()
        val account = ledger.createAccount("Alice", BigDecimal(100))

        assertEquals("Alice", account.id)
        assertEquals(BigDecimal(100), account.balance)
    }

    @Test
    fun `Ledger should retrieve created account`() {
        val ledger = Ledger()
        ledger.createAccount("Alice", BigDecimal(100))

        val account = ledger.getAccount("Alice")
        assertNotNull(account)
        assertEquals("Alice", account.id)
    }

    @Test
    fun `Ledger should return null for non-existent account`() {
        val ledger = Ledger()
        assertNull(ledger.getAccount("NonExistent"))
    }

    @Test
    fun `Deposit command should increase account balance`() {
        val ledger = Ledger()
        val account = ledger.createAccount("Alice", BigDecimal(100))

        val cmd = Deposit(account, BigDecimal(50), ledger)
        cmd.execute()

        assertEquals(BigDecimal(150), account.balance)
    }

    @Test
    fun `Withdraw command should decrease account balance`() {
        val ledger = Ledger()
        val account = ledger.createAccount("Alice", BigDecimal(100))

        val cmd = Withdraw(account, BigDecimal(30), ledger)
        cmd.execute()

        assertEquals(BigDecimal(70), account.balance)
    }

    @Test
    fun `Withdraw command should fail with insufficient funds`() {
        val ledger = Ledger()
        val account = ledger.createAccount("Alice", BigDecimal(50))

        val cmd = Withdraw(account, BigDecimal(100), ledger)
        cmd.execute()

        assertEquals(BigDecimal(50), account.balance)
    }

    @Test
    fun `Transfer command should move funds between accounts`() {
        val ledger = Ledger()
        val alice = ledger.createAccount("Alice", BigDecimal(100))
        val bob = ledger.createAccount("Bob", BigDecimal(50))

        val cmd = Transfer(alice, bob, BigDecimal(30), ledger)
        cmd.execute()

        assertEquals(BigDecimal(70), alice.balance)
        assertEquals(BigDecimal(80), bob.balance)
    }

    @Test
    fun `Transfer command should fail with insufficient funds`() {
        val ledger = Ledger()
        val alice = ledger.createAccount("Alice", BigDecimal(50))
        val bob = ledger.createAccount("Bob", BigDecimal(50))

        val cmd = Transfer(alice, bob, BigDecimal(100), ledger)
        cmd.execute()

        assertEquals(BigDecimal(50), alice.balance)
        assertEquals(BigDecimal(50), bob.balance)
    }

    @Test
    fun `Ledger should record transactions`() {
        val ledger = Ledger()
        val account = ledger.createAccount("Alice", BigDecimal(100))

        val cmd = Deposit(account, BigDecimal(50), ledger)
        cmd.execute()

        ledger.printTransactions()
    }
}
