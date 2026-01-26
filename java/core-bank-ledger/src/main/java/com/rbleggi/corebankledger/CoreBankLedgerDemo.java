package com.rbleggi.corebankledger;

import java.math.BigDecimal;
import java.util.*;

interface LedgerCommand {
    void execute();
}

class Account {
    private final String id;
    private BigDecimal balance;

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

class Ledger {
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> transactions = new ArrayList<>();

    public Account createAccount(String id, BigDecimal initial) {
        Account account = new Account(id, initial);
        accounts.put(id, account);
        return account;
    }

    public Account createAccount(String id) {
        return createAccount(id, BigDecimal.ZERO);
    }

    public Account getAccount(String id) {
        return accounts.get(id);
    }

    public void record(String tx) {
        transactions.add(tx);
    }

    public void printAccounts() {
        accounts.values().forEach(account ->
            System.out.println(account.getId() + ": " + account.getBalance())
        );
    }

    public void printTransactions() {
        transactions.forEach(System.out::println);
    }
}

record Deposit(Account account, BigDecimal amount, Ledger ledger) implements LedgerCommand {
    @Override
    public void execute() {
        account.setBalance(account.getBalance().add(amount));
        ledger.record("Deposit " + amount + " to " + account.getId());
    }
}

record Withdraw(Account account, BigDecimal amount, Ledger ledger) implements LedgerCommand {
    @Override
    public void execute() {
        if (account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            ledger.record("Withdraw " + amount + " from " + account.getId());
        } else {
            ledger.record("Failed withdraw " + amount + " from " + account.getId() + ": insufficient funds");
        }
    }
}

record Transfer(Account from, Account to, BigDecimal amount, Ledger ledger) implements LedgerCommand {
    @Override
    public void execute() {
        if (from.getBalance().compareTo(amount) >= 0) {
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));
            ledger.record("Transfer " + amount + " from " + from.getId() + " to " + to.getId());
        } else {
            ledger.record("Failed transfer " + amount + " from " + from.getId() + " to " + to.getId() + ": insufficient funds");
        }
    }
}

public class CoreBankLedgerDemo {
    public static void main(String[] args) {
        System.out.println("Core Bank Ledger Demo");
    }
}
