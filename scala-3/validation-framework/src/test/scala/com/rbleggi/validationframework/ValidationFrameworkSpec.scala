package com.rbleggi.validationframework

import org.scalatest.funsuite.AnyFunSuite

class ValidationFrameworkSpec extends AnyFunSuite {
  test("ValidatorChain should validate all rules and stop on first failure") {
    val chain = new ValidatorChain()
    chain.addRule((input: String) => if (input.nonEmpty) Right(()) else Left("Empty"))
    chain.addRule((input: String) => if (input.length > 3) Right(()) else Left("Too short"))
    assert(chain.validate("abcd").isRight)
    assert(chain.validate("").isLeft)
    assert(chain.validate("ab").left.get == "Too short")
  }
}
