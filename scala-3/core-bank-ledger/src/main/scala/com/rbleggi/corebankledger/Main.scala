package com.rbleggi.corebankledger

trait LedgerCommand {
  def execute(): Unit
}

case class Account(id: String, var balance: BigDecimal)

class Ledger {
  private var accounts: Map[String, Account] = Map.empty
  private var transactions: List[String] = List.empty

  def createAccount(id: String, initial: BigDecimal = 0): Account = {
    val acc = Account(id, initial)
    accounts += (id -> acc)
    acc
  }

  def getAccount(id: String): Option[Account] = accounts.get(id)

  def record(tx: String): Unit = transactions ::= tx
  def printAccounts(): Unit = accounts.values.foreach(a => println(s"${a.id}: ${a.balance}"))
  def printTransactions(): Unit = transactions.reverse.foreach(println)
}

case class Deposit(account: Account, amount: BigDecimal, ledger: Ledger) extends LedgerCommand {
  def execute(): Unit = {
    account.balance += amount
    ledger.record(s"Deposit $amount to ${account.id}")
  }
}

case class Withdraw(account: Account, amount: BigDecimal, ledger: Ledger) extends LedgerCommand {
  def execute(): Unit = {
    if (account.balance >= amount) {
      account.balance -= amount
      ledger.record(s"Withdraw $amount from ${account.id}")
    } else {
      ledger.record(s"Failed withdraw $amount from ${account.id}: insufficient funds")
    }
  }
}

case class Transfer(from: Account, to: Account, amount: BigDecimal, ledger: Ledger) extends LedgerCommand {
  def execute(): Unit = {
    if (from.balance >= amount) {
      from.balance -= amount
      to.balance += amount
      ledger.record(s"Transfer $amount from ${from.id} to ${to.id}")
    } else {
      ledger.record(s"Failed transfer $amount from ${from.id} to ${to.id}: insufficient funds")
    }
  }
}

@main def run(): Unit =
  println("Core Bank Ledger")
