package com.rbleggi.imageclassification.augmentation

import ai.djl.ndarray.{NDArray, NDManager}
import ai.djl.ndarray.types.Shape
import com.rbleggi.imageclassification.model.AugmentationType
import scala.util.Random

class DataAugmentation(manager: NDManager):

  def applyAugmentation(image: NDArray, augmentationType: AugmentationType): NDArray =
    augmentationType match
      case AugmentationType.HorizontalFlip => horizontalFlip(image)
      case AugmentationType.VerticalFlip => verticalFlip(image)
      case AugmentationType.Rotation => rotate(image, Random.nextInt(360))
      case AugmentationType.Brightness => adjustBrightness(image, 0.8f + Random.nextFloat() * 0.4f)
      case AugmentationType.Contrast => adjustContrast(image, 0.8f + Random.nextFloat() * 0.4f)
      case AugmentationType.Crop => randomCrop(image, 0.8f)
      case AugmentationType.Zoom => zoom(image, 0.9f + Random.nextFloat() * 0.2f)

  def applyMultipleAugmentations(image: NDArray, augmentations: List[AugmentationType]): NDArray =
    augmentations.foldLeft(image) { (img, aug) =>
      applyAugmentation(img, aug)
    }

  private def horizontalFlip(image: NDArray): NDArray =
    image.flip(2)

  private def verticalFlip(image: NDArray): NDArray =
    image.flip(1)

  private def rotate(image: NDArray, degrees: Int): NDArray =
    val radians = Math.toRadians(degrees).toFloat
    val cos = Math.cos(radians).toFloat
    val sin = Math.sin(radians).toFloat

    val shape = image.getShape
    val height = shape.get(1).toInt
    val width = shape.get(2).toInt

    val centerX = width / 2.0f
    val centerY = height / 2.0f

    val result = manager.zeros(shape)

    for
      y <- 0 until height
      x <- 0 until width
    do
      val relX = x - centerX
      val relY = y - centerY

      val newX = (relX * cos - relY * sin + centerX).toInt
      val newY = (relX * sin + relY * cos + centerY).toInt

      if newX >= 0 && newX < width && newY >= 0 && newY < height then
        result.set(Array(0, y.toLong, x.toLong), image.get(Array(0, newY.toLong, newX.toLong)))

    result

  private def adjustBrightness(image: NDArray, factor: Float): NDArray =
    image.mul(factor).clip(0, 255)

  private def adjustContrast(image: NDArray, factor: Float): NDArray =
    val mean = image.mean().getFloat()
    image.sub(mean).mul(factor).add(mean).clip(0, 255)

  private def randomCrop(image: NDArray, scale: Float): NDArray =
    val shape = image.getShape
    val height = shape.get(1).toInt
    val width = shape.get(2).toInt

    val newHeight = (height * scale).toInt
    val newWidth = (width * scale).toInt

    val startY = Random.nextInt(height - newHeight)
    val startX = Random.nextInt(width - newWidth)

    image.get(s":,$startY:${startY + newHeight},$startX:${startX + newWidth}")

  private def zoom(image: NDArray, factor: Float): NDArray =
    val shape = image.getShape
    val height = shape.get(1).toInt
    val width = shape.get(2).toInt

    val newHeight = (height * factor).toInt
    val newWidth = (width * factor).toInt

    val startY = (height - newHeight) / 2
    val startX = (width - newWidth) / 2

    image.get(s":,$startY:${startY + newHeight},$startX:${startX + newWidth}")

object DataAugmentation:
  def apply(manager: NDManager): DataAugmentation = new DataAugmentation(manager)
