package com.rbleggi.imageclassification

import scala.util.Random
import scala.math._

case class Matrix(data: Array[Array[Double]]):
  val rows: Int = data.length
  val cols: Int = if rows > 0 then data(0).length else 0

  def apply(i: Int, j: Int): Double = data(i)(j)
  def update(i: Int, j: Int, value: Double): Unit = data(i)(j) = value

  def +(other: Matrix): Matrix =
    Matrix(data.zip(other.data).map((r1, r2) => r1.zip(r2).map(_ + _)))

  def -(other: Matrix): Matrix =
    Matrix(data.zip(other.data).map((r1, r2) => r1.zip(r2).map(_ - _)))

  def *(scalar: Double): Matrix =
    Matrix(data.map(_.map(_ * scalar)))

  def dot(other: Matrix): Matrix =
    val result = Array.ofDim[Double](rows, other.cols)
    for
      i <- 0 until rows
      j <- 0 until other.cols
      k <- 0 until cols
    do result(i)(j) += this(i, k) * other(k, j)
    Matrix(result)

  def transpose: Matrix =
    Matrix(Array.tabulate(cols, rows)((j, i) => this(i, j)))

  def map(f: Double => Double): Matrix =
    Matrix(data.map(_.map(f)))

object Matrix:
  def zeros(rows: Int, cols: Int): Matrix =
    Matrix(Array.fill(rows, cols)(0.0))

  def random(rows: Int, cols: Int, scale: Double = 0.01): Matrix =
    val rng = Random()
    Matrix(Array.fill(rows, cols)((rng.nextGaussian() * scale)))

case class Image(pixels: Array[Double], width: Int, height: Int, channels: Int):
  def toMatrix: Matrix =
    Matrix(Array(pixels))

@main def runSimpleImageClassifier(): Unit =
  println("=== Simple Image Classification ===\n")
  println("Basic structure created with Matrix and Image classes")
