package com.rbleggi.imageclassification.utils

class EarlyStopping(patience: Int = 5, minDelta: Float = 0.0001f):

  private var bestScore: Option[Float] = None
  private var counter: Int = 0
  private var shouldStop: Boolean = false

  def checkImprovement(currentScore: Float, higherIsBetter: Boolean = true): Boolean =
    val improved = bestScore match
      case None =>
        bestScore = Some(currentScore)
        true
      case Some(best) =>
        val delta = if higherIsBetter then currentScore - best else best - currentScore
        if delta > minDelta then
          bestScore = Some(currentScore)
          counter = 0
          true
        else
          counter += 1
          false

    if counter >= patience then
      shouldStop = true

    improved

  def shouldStopTraining: Boolean = shouldStop

  def reset(): Unit =
    bestScore = None
    counter = 0
    shouldStop = false

  def getBestScore: Option[Float] = bestScore

  def getCounter: Int = counter

object EarlyStopping:
  def apply(patience: Int = 5, minDelta: Float = 0.0001f): EarlyStopping =
    new EarlyStopping(patience, minDelta)
