package com.rbleggi.imageclassification.utils

trait LearningRateScheduler:
  def getLearningRate(epoch: Int): Float

class ConstantLR(initialLR: Float) extends LearningRateScheduler:
  override def getLearningRate(epoch: Int): Float = initialLR

class StepDecayLR(initialLR: Float, dropRate: Float, epochsDrop: Int) extends LearningRateScheduler:
  override def getLearningRate(epoch: Int): Float =
    initialLR * Math.pow(dropRate, Math.floor((1 + epoch) / epochsDrop.toDouble)).toFloat

class ExponentialDecayLR(initialLR: Float, decayRate: Float) extends LearningRateScheduler:
  override def getLearningRate(epoch: Int): Float =
    initialLR * Math.exp(-decayRate * epoch).toFloat

class CosineAnnealingLR(initialLR: Float, minLR: Float, totalEpochs: Int) extends LearningRateScheduler:
  override def getLearningRate(epoch: Int): Float =
    minLR + (initialLR - minLR) * (1 + Math.cos(Math.PI * epoch / totalEpochs)).toFloat / 2

class PolynomialDecayLR(initialLR: Float, endLR: Float, totalEpochs: Int, power: Float = 1.0f) extends LearningRateScheduler:
  override def getLearningRate(epoch: Int): Float =
    (initialLR - endLR) * Math.pow(1 - epoch.toFloat / totalEpochs, power).toFloat + endLR

class WarmupLR(
  baseLR: Float,
  warmupEpochs: Int,
  baseScheduler: LearningRateScheduler
) extends LearningRateScheduler:
  override def getLearningRate(epoch: Int): Float =
    if epoch < warmupEpochs then
      baseLR * (epoch + 1).toFloat / warmupEpochs
    else
      baseScheduler.getLearningRate(epoch - warmupEpochs)

object LearningRateScheduler:
  def constant(lr: Float): LearningRateScheduler = new ConstantLR(lr)

  def stepDecay(initialLR: Float, dropRate: Float = 0.5f, epochsDrop: Int = 10): LearningRateScheduler =
    new StepDecayLR(initialLR, dropRate, epochsDrop)

  def exponentialDecay(initialLR: Float, decayRate: Float = 0.1f): LearningRateScheduler =
    new ExponentialDecayLR(initialLR, decayRate)

  def cosineAnnealing(initialLR: Float, minLR: Float = 0.0f, totalEpochs: Int): LearningRateScheduler =
    new CosineAnnealingLR(initialLR, minLR, totalEpochs)

  def polynomialDecay(initialLR: Float, endLR: Float = 0.0f, totalEpochs: Int, power: Float = 1.0f): LearningRateScheduler =
    new PolynomialDecayLR(initialLR, endLR, totalEpochs, power)

  def warmup(baseLR: Float, warmupEpochs: Int, baseScheduler: LearningRateScheduler): LearningRateScheduler =
    new WarmupLR(baseLR, warmupEpochs, baseScheduler)
