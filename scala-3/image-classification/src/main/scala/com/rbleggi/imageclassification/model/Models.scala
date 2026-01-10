package com.rbleggi.imageclassification.model

import ai.djl.ndarray.NDArray
import java.nio.file.Path

case class ImageData(
  path: Path,
  label: String,
  category: Int
)

case class ClassificationResult(
  predictedLabel: String,
  confidence: Float,
  allProbabilities: Map[String, Float]
)

case class TrainingConfig(
  epochs: Int,
  batchSize: Int,
  learningRate: Float,
  useAugmentation: Boolean
)

case class ModelMetrics(
  accuracy: Float,
  loss: Float,
  epoch: Int
)

enum AugmentationType:
  case HorizontalFlip
  case VerticalFlip
  case Rotation
  case Brightness
  case Contrast
  case Crop
  case Zoom
