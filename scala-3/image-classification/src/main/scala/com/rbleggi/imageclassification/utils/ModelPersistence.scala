package com.rbleggi.imageclassification.utils

import ai.djl.Model
import ai.djl.ndarray.{NDArray, NDManager, NDList}
import java.nio.file.{Files, Path, Paths}
import java.io.{DataOutputStream, FileOutputStream, DataInputStream, FileInputStream}

class ModelPersistence:

  def saveWeights(weights: NDArray, bias: NDArray, path: Path): Unit =
    val weightsArray = weights.toFloatArray
    val biasArray = bias.toFloatArray

    val out = new DataOutputStream(new FileOutputStream(path.toFile))

    out.writeInt(weightsArray.length)
    weightsArray.foreach(out.writeFloat)

    out.writeInt(biasArray.length)
    biasArray.foreach(out.writeFloat)

    out.close()

  def loadWeights(path: Path, manager: NDManager): (NDArray, NDArray) =
    val in = new DataInputStream(new FileInputStream(path.toFile))

    val weightsLength = in.readInt()
    val weightsArray = Array.fill(weightsLength)(in.readFloat())

    val biasLength = in.readInt()
    val biasArray = Array.fill(biasLength)(in.readFloat())

    in.close()

    val weights = manager.create(weightsArray)
    val bias = manager.create(biasArray)

    (weights, bias)

  def saveModel(model: Model, path: Path): Unit =
    Files.createDirectories(path.getParent)
    model.save(path, "model")

  def loadModel(path: Path): Model =
    val model = Model.newInstance("loaded-model")
    model.load(path)
    model

  def modelExists(path: Path): Boolean =
    Files.exists(path)

  def deleteModel(path: Path): Unit =
    if Files.exists(path) then
      Files.delete(path)

object ModelPersistence:
  def apply(): ModelPersistence = new ModelPersistence
