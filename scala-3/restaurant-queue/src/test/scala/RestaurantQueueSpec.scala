package com.rbleggi.restaurantqueue

import org.scalatest.funsuite.AnyFunSuite

class RestaurantQueueSpec extends AnyFunSuite {
  test("BurgerCommand should have correct name and prep time") {
    val burger = new BurgerCommand()
    assert(burger.name == "Burger")
    assert(burger.prepTime() == 12)
  }

  test("PastaCommand should have correct name and prep time") {
    val pasta = new PastaCommand()
    assert(pasta.name == "Pasta")
    assert(pasta.prepTime() == 15)
  }

  test("SaladCommand should have correct name and prep time") {
    val salad = new SaladCommand()
    assert(salad.name == "Salad")
    assert(salad.prepTime() == 5)
  }

  test("SushiCommand should have correct name and prep time") {
    val sushi = new SushiCommand()
    assert(sushi.name == "Sushi")
    assert(sushi.prepTime() == 20)
  }

  test("KitchenQueue should add and process commands") {
    val queue = new KitchenQueue()
    queue.addCommand(new BurgerCommand())
    queue.addCommand(new SaladCommand())

    assertNoException(queue.runQueue())
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

