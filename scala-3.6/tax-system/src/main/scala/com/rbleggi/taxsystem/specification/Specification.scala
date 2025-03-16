package com.rbleggi.taxsystem.specification

import com.rbleggi.taxsystem.model.{State, TaxRule, Product}

trait Specification[T]:
  def matches(candidate: T): Boolean

  def and(other: Specification[T]): Specification[T] =
    (candidate: T) => this.matches(candidate) && other.matches(candidate)

  def or(other: Specification[T]): Specification[T] =
    (candidate: T) => this.matches(candidate) || other.matches(candidate)

class ProductSpecification(expectedProduct: Product) extends Specification[TaxRule]:
  override def matches(rule: TaxRule): Boolean = rule.product == expectedProduct

class StateSpecification(expectedState: State) extends Specification[TaxRule]:
  override def matches(rule: TaxRule): Boolean = rule.state == expectedState

class YearSpecification(expectedYear: Int) extends Specification[TaxRule]:
  override def matches(rule: TaxRule): Boolean = rule.year == expectedYear
