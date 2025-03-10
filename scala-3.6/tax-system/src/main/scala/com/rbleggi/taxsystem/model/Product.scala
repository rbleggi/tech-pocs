package com.rbleggi.taxsystem.model

sealed trait Product {
  def name: String
  def price: Double
}

final case class Electronic(name: String, price: Double) extends Product
final case class Book(name: String, price: Double) extends Product
final case class Food(name: String, price: Double) extends Product
