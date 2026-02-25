package com.rbleggi.corebankledger

import java.math.BigDecimal

interface LedgerCommand {
    fun execute()
}

data class Account(val id: String, var balance: BigDecimal)

class Ledger {
    private val accounts: MutableMap<String, Account> = mutableMapOf()
    private val transactions: MutableList<String> = mutableListOf()

    fun createAccount(id: String, initial: BigDecimal = BigDecimal.ZERO): Account {
        val account = Account(id, initial)
        accounts[id] = account
        return account
    }

    fun getAccount(id: String): Account? = accounts[id]

    fun record(tx: String) {
        transactions.add(tx)
    }

    fun printAccounts() {
        accounts.values.forEach { println("${it.id}: ${it.balance}") }
    }

    fun printTransactions() {
        transactions.forEach { println(it) }
    }
}

data class Deposit(
    val account: Account,
    val amount: BigDecimal,
    val ledger: Ledger
) : LedgerCommand {
    override fun execute() {
        account.balance = account.balance.add(amount)
        ledger.record("Deposit $amount to ${account.id}")
    }
}

data class Withdraw(
    val account: Account,
    val amount: BigDecimal,
    val ledger: Ledger
) : LedgerCommand {
    override fun execute() {
        if (account.balance >= amount) {
            account.balance = account.balance.subtract(amount)
            ledger.record("Withdraw $amount from ${account.id}")
        } else {
            ledger.record("Failed withdraw $amount from ${account.id}: insufficient funds")
        }
    }
}

data class Transfer(
    val from: Account,
    val to: Account,
    val amount: BigDecimal,
    val ledger: Ledger
) : LedgerCommand {
    override fun execute() {
        if (from.balance >= amount) {
            from.balance = from.balance.subtract(amount)
            to.balance = to.balance.add(amount)
            ledger.record("Transfer $amount from ${from.id} to ${to.id}")
        } else {
            ledger.record("Failed transfer $amount from ${from.id} to ${to.id}: insufficient funds")
        }
    }
}

fun main() {
    println("Core Bank Ledger")
}
