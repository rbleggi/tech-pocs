package com.rbleggi.taxsystem.model

sealed class Product(open val name: String, open val price: Double)

data class Electronic(override val name: String, override val price: Double) : Product(name, price)
data class Book(override val name: String, override val price: Double) : Product(name, price)
data class Food(override val name: String, override val price: Double) : Product(name, price)
