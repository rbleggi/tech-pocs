package com.rbleggi.imageclassification.preprocessing

import ai.djl.ndarray.{NDArray, NDManager}
import ai.djl.ndarray.types.Shape

class ImagePreprocessor(manager: NDManager):

  def normalize(image: NDArray, mean: Array[Float], std: Array[Float]): NDArray =
    val channels = image.getShape.get(0).toInt
    require(mean.length == channels && std.length == channels,
      s"Mean and std must have same length as image channels ($channels)")

    val normalized = image.duplicate()
    for i <- 0 until channels do
      val channel = normalized.get(s"$i,:,:")
      channel.subi(mean(i)).divi(std(i))

    normalized

  def resize(image: NDArray, targetHeight: Int, targetWidth: Int): NDArray =
    val shape = image.getShape
    val currentHeight = shape.get(1).toInt
    val currentWidth = shape.get(2).toInt

    if currentHeight == targetHeight && currentWidth == targetWidth then
      image.duplicate()
    else
      val resized = manager.zeros(new Shape(shape.get(0), targetHeight, targetWidth))

      val scaleY = currentHeight.toFloat / targetHeight
      val scaleX = currentWidth.toFloat / targetWidth

      for
        y <- 0 until targetHeight
        x <- 0 until targetWidth
      do
        val srcY = (y * scaleY).toInt.min(currentHeight - 1)
        val srcX = (x * scaleX).toInt.min(currentWidth - 1)

        resized.set(Array(0, y.toLong, x.toLong), image.get(Array(0, srcY.toLong, srcX.toLong)))

      resized

  def centerCrop(image: NDArray, cropHeight: Int, cropWidth: Int): NDArray =
    val shape = image.getShape
    val height = shape.get(1).toInt
    val width = shape.get(2).toInt

    require(cropHeight <= height && cropWidth <= width,
      s"Crop dimensions ($cropHeight x $cropWidth) must be smaller than image ($height x $width)")

    val startY = (height - cropHeight) / 2
    val startX = (width - cropWidth) / 2

    image.get(s":,$startY:${startY + cropHeight},$startX:${startX + cropWidth}")

  def toGrayscale(image: NDArray): NDArray =
    val shape = image.getShape
    require(shape.get(0) == 3, "Image must have 3 channels (RGB)")

    val r = image.get("0,:,:")
    val g = image.get("1,:,:")
    val b = image.get("2,:,:")

    r.mul(0.299f).add(g.mul(0.587f)).add(b.mul(0.114f))

  def standardize(image: NDArray): NDArray =
    val mean = image.mean().getFloat()
    val std = image.sub(mean).pow(2).mean().sqrt().getFloat()

    if std > 0 then
      image.sub(mean).div(std)
    else
      image.sub(mean)

  def clip(image: NDArray, minValue: Float, maxValue: Float): NDArray =
    image.clip(minValue, maxValue)

  def applyPreprocessingPipeline(
    image: NDArray,
    resize: Option[(Int, Int)] = None,
    normalize: Option[(Array[Float], Array[Float])] = None,
    centerCrop: Option[(Int, Int)] = None,
    toGray: Boolean = false,
    standardize: Boolean = false
  ): NDArray =
    var processed = image

    resize.foreach { case (h, w) =>
      processed = this.resize(processed, h, w)
    }

    centerCrop.foreach { case (h, w) =>
      processed = this.centerCrop(processed, h, w)
    }

    if toGray then
      processed = toGrayscale(processed)

    normalize.foreach { case (mean, std) =>
      processed = this.normalize(processed, mean, std)
    }

    if standardize then
      processed = this.standardize(processed)

    processed

object ImagePreprocessor:
  def apply(manager: NDManager): ImagePreprocessor = new ImagePreprocessor(manager)

  val ImageNetMean: Array[Float] = Array(0.485f, 0.456f, 0.406f)
  val ImageNetStd: Array[Float] = Array(0.229f, 0.224f, 0.225f)
