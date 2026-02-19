package com.rbleggi.macros

import scala.quoted.*

object DebugMacros:
  inline def debug[T](inline expr: T): T = ${debugImpl('expr)}

  private def debugImpl[T: Type](expr: Expr[T])(using Quotes): Expr[T] =
    import quotes.reflect.*
    val exprString = Expr(expr.show)
    '{
      val value = $expr
      println(s"Debug: ${$exprString} = $value")
      value
    }

  inline def assertPositive(inline value: Double): Double = ${assertPositiveImpl('value)}

  private def assertPositiveImpl(value: Expr[Double])(using Quotes): Expr[Double] =
    import quotes.reflect.*
    val valueString = Expr(value.show)
    '{
      val v = $value
      if v <= 0 then
        throw new IllegalArgumentException(s"Value must be positive: ${$valueString} = $v")
      v
    }

object FieldCounter:
  inline def countFields[T]: Int = ${countFieldsImpl[T]}

  private def countFieldsImpl[T: Type](using Quotes): Expr[Int] =
    import quotes.reflect.*
    val fields = TypeRepr.of[T].typeSymbol.caseFields
    Expr(fields.size)

  inline def fieldNames[T]: List[String] = ${fieldNamesImpl[T]}

  private def fieldNamesImpl[T: Type](using Quotes): Expr[List[String]] =
    import quotes.reflect.*
    val names = TypeRepr.of[T].typeSymbol.caseFields.map(_.name)
    Expr(names)

case class Client(name: String, cpf: String, balance: Double)

case class Product(name: String, price: Double, stock: Int)

case class Address(street: String, city: String, state: String, zipCode: String)
