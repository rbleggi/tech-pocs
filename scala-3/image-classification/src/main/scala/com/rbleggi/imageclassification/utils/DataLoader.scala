package com.rbleggi.imageclassification.utils

import ai.djl.ndarray.{NDArray, NDManager}
import ai.djl.ndarray.types.Shape
import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters._

case class Dataset(images: List[NDArray], labels: List[Int], classNames: List[String])

class DataLoader(manager: NDManager):

  def loadSyntheticDataset(
    numSamples: Int,
    numClasses: Int,
    imageShape: Shape
  ): Dataset =
    val images = (1 to numSamples).map { _ =>
      manager.randomUniform(0f, 1f, imageShape)
    }.toList

    val labels = (1 to numSamples).map(_ % numClasses).toList
    val classNames = (0 until numClasses).map(i => s"class_$i").toList

    Dataset(images, labels, classNames)

  def splitDataset(
    dataset: Dataset,
    trainRatio: Float = 0.8f
  ): (Dataset, Dataset) =
    val splitIdx = (dataset.images.size * trainRatio).toInt

    val trainImages = dataset.images.take(splitIdx)
    val trainLabels = dataset.labels.take(splitIdx)

    val testImages = dataset.images.drop(splitIdx)
    val testLabels = dataset.labels.drop(splitIdx)

    (
      Dataset(trainImages, trainLabels, dataset.classNames),
      Dataset(testImages, testLabels, dataset.classNames)
    )

  def createBatches(images: List[NDArray], labels: List[Int], batchSize: Int): List[(List[NDArray], List[Int])] =
    images.zip(labels)
      .grouped(batchSize)
      .map { batch =>
        val (imgs, lbls) = batch.unzip
        (imgs, lbls)
      }
      .toList

  def shuffleDataset(dataset: Dataset): Dataset =
    val shuffled = dataset.images.zip(dataset.labels).toVector
      .sortBy(_ => scala.util.Random.nextDouble())
      .toList

    val (images, labels) = shuffled.unzip
    Dataset(images, labels, dataset.classNames)

object DataLoader:
  def apply(manager: NDManager): DataLoader = new DataLoader(manager)
