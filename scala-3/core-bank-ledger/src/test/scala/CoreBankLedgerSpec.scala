package com.rbleggi.corebankledger

class CoreBankLedgerSpec {
  test("Ledger should create account with initial balance") {
    val ledger = new Ledger()
    val account = ledger.createAccount("Alice", 100)

    assert(account.id == "Alice")
    assert(account.balance == 100)
  }

  test("Ledger should retrieve created account") {
    val ledger = new Ledger()
    ledger.createAccount("Alice", 100)

    val account = ledger.getAccount("Alice")
    assert(account.isDefined)
    assert(account.get.id == "Alice")
  }

  test("Ledger should return None for non-existent account") {
    val ledger = new Ledger()
    assert(ledger.getAccount("NonExistent").isEmpty)
  }

  test("Deposit command should increase account balance") {
    val ledger = new Ledger()
    val account = ledger.createAccount("Alice", 100)

    val cmd = Deposit(account, 50, ledger)
    cmd.execute()

    assert(account.balance == 150)
  }

  test("Withdraw command should decrease account balance") {
    val ledger = new Ledger()
    val account = ledger.createAccount("Alice", 100)

    val cmd = Withdraw(account, 30, ledger)
    cmd.execute()

    assert(account.balance == 70)
  }

  test("Withdraw command should fail with insufficient funds") {
    val ledger = new Ledger()
    val account = ledger.createAccount("Alice", 50)

    val cmd = Withdraw(account, 100, ledger)
    cmd.execute()

    assert(account.balance == 50)
  }

  test("Transfer command should move funds between accounts") {
    val ledger = new Ledger()
    val alice = ledger.createAccount("Alice", 100)
    val bob = ledger.createAccount("Bob", 50)

    val cmd = Transfer(alice, bob, 30, ledger)
    cmd.execute()

    assert(alice.balance == 70)
    assert(bob.balance == 80)
  }

  test("Transfer command should fail with insufficient funds") {
    val ledger = new Ledger()
    val alice = ledger.createAccount("Alice", 50)
    val bob = ledger.createAccount("Bob", 50)

    val cmd = Transfer(alice, bob, 100, ledger)
    cmd.execute()

    assert(alice.balance == 50)
    assert(bob.balance == 50)
  }

  test("Ledger should record transactions") {
    val ledger = new Ledger()
    val account = ledger.createAccount("Alice", 100)

    val cmd = Deposit(account, 50, ledger)
    cmd.execute()

    assertNoException(ledger.printTransactions())
  }

  def assertNoException(block: => Unit): Unit = {
    try {
      block
      assert(true)
    } catch {
      case _: Exception => fail("Unexpected exception")
    }
  }
}
